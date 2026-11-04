package org.example.classes;

import org.example.annotations.Generatable;

@Generatable
public class MyThread {
    private State state;

    public MyThread(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MyThread{" +
                "state=" + state +
                '}';
    }
}
