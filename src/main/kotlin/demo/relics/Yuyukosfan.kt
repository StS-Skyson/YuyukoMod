package demo.relics

import basemod.abstracts.CustomRelic
import com.badlogic.gdx.graphics.Texture
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.AbstractRelic
import demo.cards.yuyuko.Butterfly
import demo.cards.yuyuko.Sakura


class Yuyukosfan : CustomRelic(
        ID,
        Texture(IMAGE_PATH),
        RelicTier.STARTER,
        LandingSound.MAGICAL
) {
    companion object {
        @JvmStatic
        val ID = "Yuyuko's fan"
        val IMAGE_PATH = "images/relics/relic.png"
    }

    var sakuraAmount = 5
    var butterflyAmount = 5

    //TODO 格挡不会消失

    override fun atBattleStartPreDraw() {
        AbstractDungeon.actionManager.addToBottom(
                MakeTempCardInDrawPileAction(
                        Sakura(), sakuraAmount, true, true
                )
        )
        AbstractDungeon.actionManager.addToBottom(
                MakeTempCardInDrawPileAction(
                        Butterfly(), butterflyAmount, true, true
                )
        )
    }

    override fun getUpdatedDescription(): String {
        return DESCRIPTIONS[0]
    }

    override fun makeCopy(): AbstractRelic {
        return Yuyukosfan()
    }


}