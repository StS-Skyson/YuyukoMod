package yuyuko.powers

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.actions.utility.UseCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardQueueItem
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import yuyuko.cards.isButterfly
import yuyuko.cards.yuyuko.Butterfly
import yuyuko.event.DegradeEvent
import yuyuko.event.EventDispenser
import yuyuko.event.Observer
import kotlin.math.max
import kotlin.math.min

class GhastlyDreamPower(amount: Int) : AbstractPower() {

    companion object {
        @JvmStatic
        val POWER_ID = "Ghastly Dream"
        private val POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID)
        val NAME = POWER_STRINGS.NAME!!
        val DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS!!
    }

    init {
        this.name = NAME
        this.ID = POWER_ID
        this.owner = AbstractDungeon.player
        this.amount = min(max(amount, 0), 999)
        this.updateDescription()
        this.type = PowerType.BUFF
        this.isTurnBased = true
        this.img = ImageMaster.loadImage("images/powers/ghastlyDream.png")
    }

    private var observer: Observer<DegradeEvent>? = null

    override fun onInitialApplication() {
        observer = EventDispenser.subscribe(DegradeEvent.ID) {
            if (card.isButterfly()) {
                cancel()
            }
        }
    }

    override fun onRemove() {
        EventDispenser.unsubscribe(DegradeEvent.ID, observer!!)
    }

    override fun onUseCard(card: AbstractCard?, action: UseCardAction?) {
        if (card!!.isButterfly()) {
            /**
             * 保留额外打出蝶的等级
             * @see Butterfly.use 等
             */
            action!!.exhaustCard = true

            repeat(amount) {
                this.flash()
                var m: AbstractMonster? = null
                if (action.target != null) {
                    m = action.target as AbstractMonster
                }

                val tmp = card.makeStatEquivalentCopy()
                /**
                 * 避免格外打出的这张牌触发本效果
                 */
                tmp.cardID = "${tmp.cardID}_FAKE"

                AbstractDungeon.player.limbo.addToBottom(tmp)
                tmp.current_x = card.current_x
                tmp.current_y = card.current_y
                tmp.target_x = Settings.WIDTH.toFloat() / 2.0f - 300.0f * Settings.scale
                tmp.target_y = Settings.HEIGHT.toFloat() / 2.0f
                tmp.freeToPlayOnce = true
                if (m != null) {
                    tmp.calculateCardDamage(m)
                }

                tmp.purgeOnUse = true
                AbstractDungeon.actionManager.cardQueue.add(
                        CardQueueItem(tmp, m, card.energyOnUse)
                )
            }
        }
    }

    override fun atEndOfTurn(isPlayer: Boolean) {
        if (isPlayer) {
            AbstractDungeon.actionManager.addToBottom(
                    RemoveSpecificPowerAction(owner, owner, this)
            )
        }
    }

    override fun updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]
    }

}
