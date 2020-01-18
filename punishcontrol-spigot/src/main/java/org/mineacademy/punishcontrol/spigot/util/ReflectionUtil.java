package org.mineacademy.punishcontrol.spigot.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@UtilityClass
public final class ReflectionUtil {


	private final Map<Map.Entry<Class, String>, Field> CACHED_FIELDS = new HashMap<>();

	public Class<?> getClass(final String classname) throws ClassNotFoundException {
		final String path = classname.replace("{nms}", "net.minecraft.server." + getVersion()).replace("{obc}", "org.bukkit.craftbukkit." + getVersion())
				.replace("{nm}", "net.minecraft." + getVersion());
		return Class.forName(path);
	}

	public String getVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}

	public Object getNMSPlayer(final Player p)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Method getHandle = p.getClass().getMethod("getHandle");
		return getHandle.invoke(p);
	}

	public Object getOBCPlayer(final Player p)
			throws SecurityException, IllegalArgumentException, ClassNotFoundException {
		return getClass("{obc}.entity.CraftPlayer").cast(p);
	}

	public Object getNMSWorld(final World w)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Method getHandle = w.getClass().getMethod("getHandle");
		return getHandle.invoke(w);
	}

	public Object getNMSScoreboard(final Scoreboard s)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Method getHandle = s.getClass().getMethod("getHandle");
		return getHandle.invoke(s);
	}

	public Object getFieldValue(final Object instance, final String fieldName)
			throws IllegalArgumentException, IllegalAccessException, SecurityException {
		final Map.Entry<Class, String> key = new AbstractMap.SimpleEntry<>(instance.getClass(), fieldName);
		final Field field = CACHED_FIELDS.computeIfAbsent(key, i -> {
			try {
				return instance.getClass().getDeclaredField(fieldName);
			} catch (final NoSuchFieldException e) {
				e.printStackTrace();
			}
			return null;
		});
		if (field == null) {
			return null;
		}
		if (!field.isAccessible()) {
			field.setAccessible(true);
		}
		return field.get(instance);
	}

	public <T> T getFieldValue(final Field field, final Object obj) {
		try {
			return (T) field.get(obj);
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Field getField(final Class<?> clazz, final String fieldName) throws Exception {
		final Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		return field;
	}

	public void setValue(final Object instance, final String field, final Object value) {
		try {
			final Field f = instance.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(instance, value);
		} catch (final Throwable t) {
			t.printStackTrace();
		}
	}

	public void setValue(final Class c, final Object instance, final String field, final Object value) {
		try {
			final Field f = c.getDeclaredField(field);
			f.setAccessible(true);
			f.set(instance, value);
		} catch (final Throwable t) {
			t.printStackTrace();
		}
	}

	public void setValueSubclass(final Class<?> clazz, final Object instance, final String field, final Object value) {
		try {
			final Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			f.set(instance, value);
		} catch (final Throwable t) {
			t.printStackTrace();
		}
	}

	public void sendAllPacket(final Object packet) throws Exception {
		for (final Player p : Bukkit.getOnlinePlayers()) {
			final Object nmsPlayer = getNMSPlayer(p);
			final Object connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
			connection.getClass().getMethod("sendPacket", ReflectionUtil.getClass("{nms}.Packet")).invoke(connection, packet);
		}
	}

	public void sendListPacket(final List<String> players, final Object packet) {
		try {
			for (final String name : players) {
				final Object nmsPlayer = getNMSPlayer(Bukkit.getPlayer(name));
				final Object connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
				connection.getClass().getMethod("sendPacket", ReflectionUtil.getClass("{nms}.Packet")).invoke(connection, packet);
			}
		} catch (final Throwable t) {
			t.printStackTrace();
		}
	}

	public void sendPlayerPacket(final Player p, final Object packet) throws Exception {
		final Object nmsPlayer = getNMSPlayer(p);
		final Object connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
		connection.getClass().getMethod("sendPacket", ReflectionUtil.getClass("{nms}.Packet")).invoke(connection, packet);
	}

	public void listFields(final Object e) {
		System.out.println(e.getClass().getName() + " contains " + e.getClass().getDeclaredFields().length + " declared fields.");
		System.out.println(e.getClass().getName() + " contains " + e.getClass().getDeclaredClasses().length + " declared classes.");
		final Field[] fds = e.getClass().getDeclaredFields();
		for (final Field fd : fds) {
			fd.setAccessible(true);
			try {
				System.out.println(fd.getName() + " -> " + fd.get(e));
			} catch (final IllegalArgumentException | IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public Object getFieldValue(final Class<?> superclass, final Object instance, final String fieldName) throws IllegalAccessException, NoSuchFieldException {
		final Field field = superclass.getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(instance);
	}
}