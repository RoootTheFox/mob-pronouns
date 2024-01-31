package gay.rooot.mobpronouns.mixin;

import gay.rooot.mobpronouns.stuff.PronounHelper;
import gay.rooot.mobpronouns.stuff.WokeInterface;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin implements WokeInterface {
    @Shadow public abstract UUID getUuid();

    @Unique
    private String pronouns = null;

    @Override
    public String getPronouns() {
        if (pronouns == null) {
            pronouns = PronounHelper.getEntityPronouns(getUuid().hashCode());
        }
        return pronouns;
    }
}
