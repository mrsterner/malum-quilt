package dev.sterner.malum.common.item.equipment.trinket;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.sterner.malum.api.interfaces.item.SpiritCollectActivity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class CurioWaterNecklace extends TrinketItem implements SpiritCollectActivity {
    public CurioWaterNecklace(Settings settings) {
        super(settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        Multimap<EntityAttribute, EntityAttributeModifier> map = HashMultimap.create();

        /*TODO forge ability
        map.put(ForgeMod.SWIM_SPEED.get(), new EntityAttributeModifier(uuid, "Swim Speed", 0.15f, EntityAttributeModifier.Operation.ADDITION) {
            @Override
            public double getValue() {
                if (slotContext.entity() != null) {
                    if (slotContext.entity().hasEffect(MobEffects.CONDUIT_POWER)) {
                        return 0.45f;
                    }
                }
                return super.getValue();
            }
        });
        return map;

         */
        return map;
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);
    }

    /*TODO forge event curio
    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        if (livingEntity.level.getGameTime() % 40L == 0 && livingEntity.isSwimming() && livingEntity.hasEffect(MobEffects.CONDUIT_POWER)) {
            AttributeInstance attribute = livingEntity.getAttribute(ForgeMod.SWIM_SPEED.get());
            if (attribute != null) {
                attribute.setDirty();
            }
        }
    }

    @Override
    public void takeDamageEvent(LivingHurtEvent event, LivingEntity attacker, LivingEntity attacked, ItemStack stack) {
        if (attacked.hasEffect(MobEffects.CONDUIT_POWER)) {
            event.setAmount(event.getAmount() * 0.5f);
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity.level.getGameTime() % 20L == 0) {
            if (entity.hasEffect(MobEffects.CONDUIT_POWER)) {
                entity.heal(2);
            }
        }
    }

     */
}
