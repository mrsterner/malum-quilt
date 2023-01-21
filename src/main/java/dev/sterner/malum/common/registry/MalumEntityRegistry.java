package dev.sterner.malum.common.registry;

import com.sammy.lodestone.helpers.DataHelper;
import dev.sterner.malum.common.entity.SpiritItemEntity;
import dev.sterner.malum.common.entity.boomerang.ScytheBoomerangEntity;
import dev.sterner.malum.common.entity.nitrate.EthericNitrateEntity;
import dev.sterner.malum.common.entity.nitrate.VividNitrateEntity;
import dev.sterner.malum.common.entity.spirit.MirrorItemEntity;
import dev.sterner.malum.common.entity.spirit.SoulEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

import static dev.sterner.malum.Malum.MODID;

public class MalumEntityRegistry {
	public static Map<Identifier, EntityType<?>> ENTITY_TYPES = new LinkedHashMap<>();

	public static EntityType<ScytheBoomerangEntity> SCYTHE_BOOMERANG = register("scythe_boomerang", EntityType.Builder.<ScytheBoomerangEntity>create((e, w)->new ScytheBoomerangEntity(w), SpawnGroup.MISC).setDimensions(2.5F, 0.75F).maxTrackingRange(10).build(DataHelper.prefix("scythe_boomerang").toString()));
	public static EntityType<SpiritItemEntity>      NATURAL_SPIRIT   = register("natural_spirit", EntityType.Builder.<SpiritItemEntity>create((e, w)->new SpiritItemEntity(w), SpawnGroup.MISC).setDimensions(0.5F, 0.75F).maxTrackingRange(10).build(DataHelper.prefix("natural_spirit").toString()));

	public static EntityType<EthericNitrateEntity>      ETHERIC_NITRATE   = register("etheric_nitrate", EntityType.Builder.<EthericNitrateEntity>create((e, w)->new EthericNitrateEntity(w), SpawnGroup.MISC)
			.setDimensions(0.5F, 0.5F)
			.maxTrackingRange(20)
			.build(DataHelper.prefix("etheric_nitrate").toString()));

	public static EntityType<VividNitrateEntity>      VIVID_NITRATE   = register("vivid_nitrate", EntityType.Builder.<VividNitrateEntity>create((e, w)->new VividNitrateEntity(w), SpawnGroup.MISC)
			.setDimensions(0.5F, 0.5F)
			.maxTrackingRange(20)
			.build(DataHelper.prefix("vivid_nitrate").toString()));

	public static EntityType<MirrorItemEntity>      MIRROR_ITEM   = register("mirror_item", EntityType.Builder.<MirrorItemEntity>create((e, w)->new MirrorItemEntity(w), SpawnGroup.MISC)
			.setDimensions(0.5F, 0.5F)
			.maxTrackingRange(10)
			.build(DataHelper.prefix("mirror_item").toString()));

	public static EntityType<SoulEntity>      NATURAL_SOUL   = register("natural_soul", EntityType.Builder.<SoulEntity>create((e, w)->new SoulEntity(w), SpawnGroup.MISC)
			.setDimensions(1.5F, 1.5F)
			.maxTrackingRange(10)
			.build(DataHelper.prefix("natural_soul").toString()));

	static <T extends Entity> EntityType<T> register(String id, EntityType<T> type) {
		ENTITY_TYPES.put(new Identifier(MODID, id), type);
		return type;
	}

	public static void init() {
		ENTITY_TYPES.forEach((id, entityType) -> Registry.register(Registries.ENTITY_TYPE, id, entityType));
	}
}