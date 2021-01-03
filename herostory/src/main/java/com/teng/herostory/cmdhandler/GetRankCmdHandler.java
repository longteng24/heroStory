package com.teng.herostory.cmdhandler;

import com.teng.herostory.msg.GameMsgProtocol;
import com.teng.herostory.rank.RankItem;
import com.teng.herostory.rank.RankService;
import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;
import java.util.Collections;

/**
 * @program: nettyProject
 * @description: 获取排行榜指令处理器
 * @author: Mr.Teng
 * @create: 2021-01-03 09:43
 **/
public class GetRankCmdHandler implements ICmdHandler<GameMsgProtocol.GetRankCmd>{
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.GetRankCmd cmd) {
        if (null == ctx ||
                null == cmd) {
            return;
        }

        RankService.getInstance().getRank((rankItems) -> {
            if (null == rankItems) {
                rankItems = Collections.emptyList();
            }

            GameMsgProtocol.GetRankResult.Builder resultBuilder = GameMsgProtocol.GetRankResult.newBuilder();

            for (RankItem rankItem : rankItems) {
                if (null == rankItem) {
                    continue;
                }
                GameMsgProtocol.GetRankResult.RankItem.Builder rankItemBuilder = GameMsgProtocol.GetRankResult.RankItem.newBuilder();
                rankItemBuilder.setRankId(rankItem.getRankId());
                rankItemBuilder.setUserId(rankItem.getUserId());
                rankItemBuilder.setUserName(rankItem.getUserName());
                rankItemBuilder.setHeroAvatar(rankItem.getHeroAvatar());
                rankItemBuilder.setWin(rankItem.getWin());

                resultBuilder.addRankItem(rankItemBuilder);

            }

            GameMsgProtocol.GetRankResult newResult = resultBuilder.build();
            ctx.writeAndFlush(newResult);

            return null;
        });
    }
}
