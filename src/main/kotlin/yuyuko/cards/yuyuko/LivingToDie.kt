package yuyuko.cards.yuyuko

import basemod.abstracts.CustomCard
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import yuyuko.patches.CardColorEnum
import yuyuko.powers.LivingToDiePower

class LivingToDie : CustomCard(
        ID, NAME, IMAGE_PATH, COST, DESCRIPTION,
        CardType.POWER, CardColorEnum.YUYUKO_COLOR,
        CardRarity.RARE, CardTarget.SELF
) {
    companion object {
        @JvmStatic
        val ID = "Living to Die"
        val IMAGE_PATH = "images/yuyuko/cards/power2.png"
        val COST = 1
        private val CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID)
        val NAME = CARD_STRINGS.NAME!!
        val DESCRIPTION = CARD_STRINGS.DESCRIPTION!!
    }


    override fun makeCopy(): AbstractCard = LivingToDie()

    override fun use(self: AbstractPlayer?, target: AbstractMonster?) {
        AbstractDungeon.actionManager.addToBottom(
                ApplyPowerAction(
                        self, self,
                        LivingToDiePower()
                )
        )

    }

    override fun upgrade() {
        if (!this.upgraded) {
            this.upgradeName()
            this.upgradeBaseCost(0)
        }
    }


}