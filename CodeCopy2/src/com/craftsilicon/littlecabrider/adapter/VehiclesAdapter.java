package com.craftsilicon.littlecabrider.adapter;

public class VehiclesAdapter {

   String v_image 	= null;
   String v_name 	= null;
   String v_type 	= null;
   boolean selected = false;

	public VehiclesAdapter(String v_name, String v_type, String v_image, boolean selected) {
		super();		
		this.v_image 	= v_image;
		this.v_name  	= v_name;
	    this.v_type  	= v_type;
	    this.selected	= selected;
	}
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public String getv_image() {
		return v_image;
	}
	public void setv_image(String image) {
		this.v_image = image;
	}

	public String getv_name() {
		return v_name;
	}
	public void setv_name(String v_name) {
		this.v_name = v_name;
	}
	
	public String getv_type() {
		return v_type;
	}
	public void setv_type(String v_type) {
		this.v_type = v_type;
	}

}