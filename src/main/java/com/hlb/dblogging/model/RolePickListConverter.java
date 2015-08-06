package com.hlb.dblogging.model;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

import com.hlb.dblogging.jpa.model.Role;

@FacesConverter(value = "rolePickListConverter")
public class RolePickListConverter implements Converter {
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
	    Object ret = null;
	    if (arg1 instanceof PickList) {
	        Object dualList = ((PickList) arg1).getValue();
	        DualListModel dl = (DualListModel) dualList;
	        for (Object o : dl.getSource()) {
	            String id = "" + ((Role) o).getId();
	            if (arg2.equals(id)) {
	                ret = o;
	                break;
	            }
	        }
	        if (ret == null)
	            for (Object o : dl.getTarget()) {
	                String id = "" + ((Role) o).getId();
	                if (arg2.equals(id)) {
	                    ret = o;
	                    break;
	                }
	            }
	    }
	    return ret;
	}
	
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
	    String str = "";
	    if (arg2 instanceof Role) {
	        str = "" + ((Role) arg2).getId();
	    }
	    return str;
	}
}
