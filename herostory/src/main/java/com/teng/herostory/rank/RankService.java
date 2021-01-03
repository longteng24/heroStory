package com.teng.herostory.rank;

import com.alibaba.fastjson.JSONObject;
import com.teng.herostory.GameMsgDecoder;
import com.teng.herostory.async.AsyncOperationProcessor;
import com.teng.herostory.async.IAsyncOperation;
import com.teng.herostory.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @program: nettyProject
 * @description: 排行榜服务
 * @author: Mr.Teng
 * @create: 2021-01-03 09:45
 **/
public class RankService {
    static final Logger LOGGER = LoggerFactory.getLogger(RankService.class);
    private static final RankService _instance = new RankService();

    /**
     * 私有化构造器
     */
    private RankService() {
    }

    /**
     * 获取单例对象
     * @return  单例对象
     */
    public static RankService getInstance() {
        return _instance;
    }

    /**
     * 获取排行榜类表
     */
    public void getRank(Function<List<RankItem>,Void> callback) {
        AsyncOperationProcessor.getInstance().process(new AsyncGetRank(){
            @Override
            public void doFinish() {
                callback.apply(this.getRankItemList());
            }
        });

    }

    private static class AsyncGetRank implements IAsyncOperation {

        /**
         * 排名条目列表
         */
        private List<RankItem> _rankItemList;

        /**
         * 获取排名条目类表
         * @return 排名条目类表
         */
        public List<RankItem> getRankItemList() {
            return _rankItemList;
        }

        @Override
        public void doAsync() {
            try (Jedis redis = RedisUtil.getJedis()) {
                //获取集合字符串  zadd Rank  1 2 (1   t.getScore(); 表示分数 ， 2  t.getElement(); 为玩家id)
              Set<Tuple> valSet=  redis.zrevrangeByScoreWithScores("Rank", 0, 9);

                List<RankItem> rankItemList = new ArrayList<>();
                int i = 0;
                for (Tuple t : valSet) {
                    if (t == null) {
                        continue;
                    }

                    //获取用户id
                  int userId=  Integer.parseInt(t.getElement());

                    //获取用户信息
              String jsonStr= redis.hget("user_" + userId, "BasicInfo");

                    if (jsonStr == null) {
                        continue;
                    }

                    RankItem newItem = new RankItem();
                    newItem.setRankId(++i);
                    newItem.setUserId(userId);
                    newItem.setWin((int)t.getScore());

                    JSONObject jsonObj = JSONObject.parseObject(jsonStr);
                    newItem.setUserName(jsonObj.getString("userName"));
                    newItem.setHeroAvatar(jsonObj.getString("heroAvatar"));

                    rankItemList.add(newItem);
                }

                _rankItemList = rankItemList;
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

}
