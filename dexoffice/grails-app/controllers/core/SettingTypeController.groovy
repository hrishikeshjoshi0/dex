package core

import grails.rest.RestfulController

class SettingTypeController extends RestfulController<SettingType> {

    static responseFormats = ['json', 'xml']
	
    SettingTypeController() {
        super(SettingType)
    }
}
