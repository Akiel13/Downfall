package guardian.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import guardian.GuardianMod;

import java.util.ArrayList;

public class PlaceCardsInHandIntoStasisAction extends AbstractGameAction {
    public static final String[] TEXT;
    private final boolean anyNumber;
    private final boolean endOfTurn;

    public PlaceCardsInHandIntoStasisAction(AbstractCreature source, int amount, boolean anyNumber) {
        this(source, amount, anyNumber, false);
    }

    public PlaceCardsInHandIntoStasisAction(AbstractCreature source, int amount, boolean anyNumber, boolean endOfTurn) {
        this.setValues(AbstractDungeon.player, source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.anyNumber = anyNumber;
        this.endOfTurn = endOfTurn;
    }

    public void update() {
        if (this.duration == 0.5F) {
            if (this.amount > AbstractDungeon.player.hand.size())
                this.amount = AbstractDungeon.player.hand.size();

            if (this.amount == 0) {
                this.isDone = true;
                return;
            }

            if (!GuardianMod.canSpawnStasisOrb()) {
                this.isDone = true;
                if (!AbstractDungeon.player.hasEmptyOrb())
                    AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[5], true));
                return;
            }

            AbstractDungeon.handCardSelectScreen.open(TEXT[3], this.amount, false, anyNumber, false, false, anyNumber);
        } else {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                ArrayList<AbstractCard> cards = AbstractDungeon.handCardSelectScreen.selectedCards.group;
                AbstractCard c;
                //Making it look nice if you have a lot of stacks is hard.
                //Refreshing the position of cards that weren't placed into Stasis (triggers last)
                addToTop(new AbstractGameAction() {
                    @Override
                    public void update() {
                        isDone = true;
                        AbstractDungeon.player.hand.refreshHandLayout();
                    }
                });
                //Placing chosen cards (except Ethereal cards for Future Plans for consistency)
                for (int i = cards.size() - 1; i >= 0; i--) {
                    c = cards.get(i);
                    if (!(endOfTurn && c.isEthereal)) {
                        addToTop(new PlaceActualCardIntoStasis(c, AbstractDungeon.player.hand, true));
                    }
                }
                //Returning cards to hand
                for (AbstractCard card : cards) {
                    AbstractDungeon.player.hand.addToTop(card);
                }
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            }
        }
        this.tickDuration();
    }

    static {
        TEXT = CardCrawlGame.languagePack.getUIString("Guardian:UIOptions").TEXT;
    }
}
