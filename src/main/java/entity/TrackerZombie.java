package entity;

import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackerZombie extends EntityZombie {

    static {
        System.out.println("Register TrackerZombie");
        getRegMap("c", "field_75625_b").put("TZombie", TrackerZombie.class);
        getRegMap("d", "field_75626_c").put(TrackerZombie.class, "TZombie");
        //getRegMap("e","field_75623_d").put(54, TrackerZombie.class);
        getRegMap("f", "field_75624_e").put(TrackerZombie.class, 54);
        getRegMap("g", "field_75622_f").put("TZombie", 54);
    }

    private static Map<Object, Object> getRegMap(String cbName, String mcpName) {
        try {
            Field field = EntityTypes.class.getDeclaredField(cbName);
            field.setAccessible(true);
            Object obj = field.get(EntityTypes.class);
            if (obj instanceof Map<?, ?>) return (Map<Object, Object>) obj;
        } catch (Throwable ignored) {
        }
        try {
            Field field = EntityTypes.class.getDeclaredField(mcpName);
            field.setAccessible(true);
            Object obj = field.get(EntityTypes.class);
            if (obj instanceof Map<?, ?>) return (Map<Object, Object>) obj;
        } catch (Throwable ignored) {
        }
        return new HashMap<>();
    }

    private double trackSpeed = 1.0D;
    private net.minecraft.server.v1_7_R4.Entity traceTarget;

    private TrackerZombie(net.minecraft.server.v1_7_R4.World world) {
        super(world);
    }

    private static void clearList(String cbName, String mcpName, Object instance) {
        try {
            Field field = PathfinderGoalSelector.class.getDeclaredField(cbName);
            field.setAccessible(true);
            Object obj = field.get(instance);
            if (obj instanceof List) ((List) obj).clear();
            return;
        } catch (Throwable ignored) {
        }
        try {
            Field field = PathfinderGoalSelector.class.getDeclaredField(mcpName);
            field.setAccessible(true);
            Object obj = field.get(instance);
            if (obj instanceof List) ((List) obj).clear();
        } catch (Throwable ignored) {
        }
    }

    public TrackerZombie(Location location, double trackSpeed) {
        this(((CraftWorld) location.getWorld()).getHandle());
        clearList("b", "field_75782_a", goalSelector);
        clearList("c", "field_75780_b", goalSelector);
        clearList("b", "field_75782_a", targetSelector);
        clearList("c", "field_75780_b", targetSelector);

        this.trackSpeed = trackSpeed;
        setPosition(location.getX(), location.getY(), location.getZ());

        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityPlayer.class, 1.0D, false));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityPlayer.class, 8.0F));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityPlayer.class, 0, true));
    }

    public void h() {
        super.h();

        Navigation navigation = getNavigation();
        PathEntity pathEntity = navigation.a(traceTarget.locX, traceTarget.locY, traceTarget.locZ);
        navigation.a(pathEntity, trackSpeed);
    }

    public void setTraceTarget(Entity entity) {
        if (entity != null) {
            traceTarget = ((CraftEntity) entity).getHandle();
        } else traceTarget = null;
    }

    public void setNMSTraceTarget(net.minecraft.server.v1_7_R4.EntityLiving entity) {
        traceTarget = entity;
    }

    public Entity getTrackTarget() {
        return traceTarget == null ? null : traceTarget.getBukkitEntity();
    }

    public net.minecraft.server.v1_7_R4.Entity getNMSTrackTarget() {
        return traceTarget;
    }

    public static Entity spawnEntity(TrackerZombie zombie) {
        boolean success = zombie.world.addEntity(zombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return success ? zombie.getBukkitEntity() : null;
    }

    private double getTrackSpeed() {
        return trackSpeed;
    }
}
