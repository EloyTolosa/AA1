/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.controller;
import controller.action;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author eloytolosa
 */
public class ProgressBarThread implements Runnable {

    controller controller;

    // variable to stop the program
    private final AtomicBoolean running = new AtomicBoolean(false);

    public ProgressBarThread(controller controller) {
        this.controller = controller;
    }
    
    public void Stop() {
        running.set(false);
    }

    @Override
    public void run() {
        // Prepare to loop infinetly
        running.set(true);
        while (running.get()) {
            for (int i = 0; i < 100; i++) {
                try {
                    controller.comunicate(action.PROGRESS, i + 1);
                    TimeUnit.MILLISECONDS.sleep(10);
                    // We don't need this i think
                    //if (i == 99) {
                      //  i = 0;
                    // }
                } catch (InterruptedException ex) {
                    ex.getMessage();
                }
            }
        }

    }

}
