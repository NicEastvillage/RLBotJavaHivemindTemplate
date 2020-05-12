package rlbotexample;

import rlbot.ControllerState;
import rlbot.Hivemind;
import rlbot.flat.GameTickPacket;
import rlbotexample.output.ControlsOutput;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MyHivemind implements Hivemind {

    public final int team;
    public Map<Integer, Drone> drones = new HashMap<>();

    public MyHivemind(int team) {
        this.team = team;
    }

    @Override
    public Map<Integer, ControllerState> processInput(Set<Integer> droneIndexes, GameTickPacket request) {

        // Check if the drones indexes changed
        if (!drones.keySet().equals(droneIndexes)) {
            refreshDroneIndexes(droneIndexes);
        }

        for (Drone drone : drones.values()) {
            drone.controls = new ControlsOutput().withThrottle(1f).withBoost().withSteer((float) Math.sin(request.gameInfo().gameTimeRemaining()));
        }

        return drones.values().stream().collect(Collectors.toMap(drone -> drone.index, drone -> drone.controls));
    }

    private void refreshDroneIndexes(Set<Integer> newDroneIndexes) {
        // Keep drones that we know of already but instantiate a new drone if they are new.
        drones = newDroneIndexes.stream().map(index -> {
            if (drones.containsKey(index)) return drones.get(index);
            else return new Drone(index);
        }).collect(Collectors.toMap(drone -> drone.index, drone -> drone));
    }

    @Override
    public void retire() {
        System.out.println("Hive retired");
    }
}
