package collector.cards.collectibles;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static collector.CollectorMod.makeID;

public class ShieldGremlinCard extends AbstractCollectibleCard {
    public final static String ID = makeID(ShieldGremlinCard.class.getSimpleName());
    // intellij stuff skill, self, common, , , 9, 3, , 

    public ShieldGremlinCard() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 6;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
    }

    public void upp() {
        upgradeBlock(3);
    }
}