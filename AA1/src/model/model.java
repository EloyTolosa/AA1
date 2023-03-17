/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.*;

/**
 *
 * @author eloytolosa
 */
public class model implements comunicable {

    private controller controller;
    private ModelCalculator mc;
    private ProgressBarThread pbt;

    public model(controller controller) {
        this.controller = controller;
    }

    /**
     *
     * @param data
     */
    @Override
    public void comunicate(Object... data) {
        action action = (action) data[0];
        switch (action) {
            case PAINT:

                controller.comunicate(data);
                break;

            case CALCULATE:

                Function function = (Function) data[1];
                mc = new ModelCalculator(function, controller);
                Thread t = new Thread(mc);
                t.start();

                pbt = new ProgressBarThread(controller);
                t = new Thread(pbt);
                t.start();
                break;

            case STOP:

                pbt.Stop();
                break;

            case PROGRESS:

                System.out.println("[MODEL] Not implemented.");
                break;
        }
    }

}
