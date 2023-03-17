/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import view.view;
import model.model;

/**
 *
 * @author eloytolosa
 */
public class controller implements comunicable {

    private model model;
    private view view;

    public controller(view view) {
        this.view = view;
    }

    public void setModel(model model) {
        this.model = model;
    }

    @Override
    public void comunicate(Object... data) {
        if (model == null) {
            model = new model(this);
        }
        // TO DO : REFACTOR 
        action action = (action) data[0];
        switch (action) {
            case PAINT, PROGRESS:
                view.comunicate(data);
                break;
            case STOP, CALCULATE:
                model.comunicate(data);
                break;
            // add cases here ...
        }
    }

}
