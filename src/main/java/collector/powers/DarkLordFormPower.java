package collector.powers;

import automaton.actions.RepeatCardAction;
import collector.cards.InevitableDemise;
import collector.cards.YouAreMine;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DarkLordFormPower extends AbstractCollectorPower {
    public static final String NAME = "DarkLordForm";
    public static final String POWER_ID = makeID(NAME);
    public static final PowerType TYPE = PowerType.BUFF;
    public static final boolean TURN_BASED = false;

    public DarkLordFormPower() {
        super(NAME, TYPE, TURN_BASED, AbstractDungeon.player, null, 1);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        flash();
        for (int i = 0; i < amount; i++) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    isDone = true;
                    AbstractMonster q = AbstractDungeon.getRandomMonster();
                    addToTop(new RepeatCardAction(q, new YouAreMine()));
                }
            });
        }
    }
}