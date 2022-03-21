package dusted.world.blocks.powder;

import arc.*;
import dusted.type.*;
import dusted.world.blocks.powder.Chute.*;
import dusted.world.interfaces.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;

public class PowderJunction extends PowderBlock {
    public int maxCharge = 16;

    public PowderJunction(String name) {
        super(name);
    }

    @Override
    public void setBars() {
        super.setBars();
        bars.add("charge", build -> {
            ChuteBuild entity = (ChuteBuild) build;
            return new Bar(() -> Core.bundle.format("bar.charge"), () -> Pal.accent, () -> ((float) entity.charge) / ((float) ((Chute) entity.block).maxCharge));
        });
    }


    public class PowderJunctionBuild extends PowderBuild implements Chargedc {
        public int charge, properCharge;

        @Override
        public void updateTile() {
            properCharge = 0;
            proximity.each(build -> {
                if (build instanceof Chargedc entity && canCharge(build, this)) {
                    properCharge = Math.min(maxCharge, Math.max(properCharge, entity.charge() - 1));
                }
            });

            charge = properCharge;
        }

        @Override
        public boolean acceptPowder(Building source, Powder powder) {
            return charge > 0;
        }

        @Override
        public Building getPowderDestination(Building source, Powder powder) {
            if (!enabled) return this;

            int dir = source.relativeTo(tile.x, tile.y);
            dir = (dir + 4) % 4;
            Building next = nearby(dir);

            if (next instanceof PowderBlockc build) {
                return build.getPowderDestination(this, powder);
            }

            return this;
        }

        @Override
        public int charge() {
            return charge;
        }

        @Override
        public int maxCharge() {
            return maxCharge;
        }

        @Override
        public void charge(int charge) {
            this.charge = charge;
        }
    }
}
