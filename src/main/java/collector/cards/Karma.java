package collector.cards;

import collector.powers.KarmaPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;

import static collector.CollectorMod.makeID;
import static collector.util.Wiz.applyToSelf;

public class Karma extends AbstractCollectorCard {
    public final static String ID = makeID(Karma.class.getSimpleName());
    // intellij stuff power, self, uncommon, , , , , , 

    public Karma() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new MetallicizePower(p, magicNumber));
        applyToSelf(new KarmaPower(secondMagic));
    }

    public void upp() {
        upgradeMagicNumber(1);
        upgradeSecondMagic(1);
    }
}