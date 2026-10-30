package com.elimentas.effect.vfx;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class VfxScheduler {

    private static final List<Task> TASKS = new ArrayList<>();

    private VfxScheduler() {}

    public static void schedule(int delayTicks, Runnable action) {
        TASKS.add(new Task(delayTicks, action));
    }

    public static void tick() {
        if (Minecraft.getInstance().level == null) {
            TASKS.clear();
            return;
        }
        List<Runnable> due = new ArrayList<>();
        Iterator<Task> it = TASKS.iterator();
        while (it.hasNext()) {
            Task task = it.next();
            if (--task.delay <= 0) {
                due.add(task.action);
                it.remove();
            }
        }
        due.forEach(Runnable::run);
    }

    private static final class Task {
        int delay;
        final Runnable action;

        Task(int delay, Runnable action) {
            this.delay = delay;
            this.action = action;
        }
    }
}
