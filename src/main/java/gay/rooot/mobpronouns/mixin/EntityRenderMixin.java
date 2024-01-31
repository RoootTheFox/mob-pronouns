package gay.rooot.mobpronouns.mixin;

import gay.rooot.mobpronouns.stuff.WokeInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRenderMixin<T extends Entity> {
    @Final
    @Shadow
    protected EntityRenderDispatcher dispatcher;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Inject(method = "render", at = @At("HEAD"))
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        renderPronouns(entity, matrices, vertexConsumers, light);
    }

    @Unique
    private void renderPronouns(T entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        String text = ((WokeInterface) entity).getPronouns();

        double distance = this.dispatcher.getSquaredDistanceToCamera(entity);
        if (distance > 64) return;

        boolean entitySneaking = entity.isSneaky();
        float labelHeight = entity.getNameLabelHeight();
        float y = 20;

        matrices.push();
        matrices.translate(0.0F, labelHeight, 0.0F);
        matrices.multiply(this.dispatcher.getRotation());
        matrices.scale(-0.0125F, -0.0125F, 0.0125F); // make it smol
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        float textBackgroundOpacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
        int backgroundColor = (int)(textBackgroundOpacity * 255.0F / 2.0F) << 24;
        TextRenderer textRenderer = this.getTextRenderer();
        float x = -textRenderer.getWidth(text) / 2.0f;

        textRenderer.draw(text, x, y, 553648127, false, matrix4f, vertexConsumers,
                entitySneaking ? TextRenderer.TextLayerType.NORMAL : TextRenderer.TextLayerType.SEE_THROUGH,
                backgroundColor, light);
        if (!entitySneaking) {
            textRenderer.draw(text, x, y, -1, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
        }

        matrices.pop();
    }
}
