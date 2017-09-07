package com.skmei.videograber;

public class VideoItem {
	
	String tittle, description, VIDEO_ID, VIDEO_ID_LD, VIDEO_ID_SD, VIDEO_ID_HD, VIDEO_ID_FHD ,duration,imgThumb, filet_type; //VIDEO_ID - url
	Double size, size_ld, size_sd, size_hd, size_fhd;
	String source;
	String privateID="";
	String downloadID;
			
	public String getDownloadID() {
		return downloadID;
	}

	public String getSource() {
		return source;
	}
	
	

	public String getPrivateID() {
		return privateID;
	}



	public void setPrivateID(String privateID) {
		this.privateID = privateID;
	}



	public void setSource(String source) {
		this.source = source;
	}

	public String getVIDEO_ID() {
		return VIDEO_ID;
	}
	
	public void setVIDEO_ID(String vIDEO_ID) {
		VIDEO_ID = vIDEO_ID;
	}

	public String getVIDEO_ID_LD() {
		return VIDEO_ID_LD;
	}
	public void setVIDEO_ID_LD(String vIDEO_ID_LD) {
		VIDEO_ID_LD = vIDEO_ID_LD;
	}

	public String getVIDEO_ID_SD() {
		return VIDEO_ID_SD;
	}
	public void setVIDEO_ID_SD(String vIDEO_ID_SD) {
		VIDEO_ID_HD = vIDEO_ID_SD;
	}

	public String getVIDEO_ID_HD() {
		return VIDEO_ID_HD;
	}
	public void setVIDEO_ID_HD(String vIDEO_ID_HD) {
		VIDEO_ID_HD = vIDEO_ID_HD;
	}

	public String getVIDEO_ID_FHD() {
		return VIDEO_ID_FHD;
	}
	public void setVIDEO_ID_FHD(String vIDEO_ID_FHD) {
		VIDEO_ID_HD = vIDEO_ID_FHD;
	}
	
	public String getDownloadName() {
				
		String temp = tittle.trim()
		.replace(".", "")
		.replace(":", "")
		.replace("?", "")		
		.replace("*", "")
		.replace(">", "")
		.replace("<", "")
		.replace("|", "")
		.replace("/", "")
		.replace("\"", "")		
		.replace("\\", "");
		
		return temp;
	}


	public VideoItem(String imgThumb, String tittle, String description,
					 String vIDEO_ID, String vIDEO_ID_SD, String vIDEO_ID_LD, String vIDEO_ID_HD, String vIDEO_ID_FHD,
					 Double size, Double size_sd, Double size_ld, Double size_hd, Double size_fhd,
					 String duration, String downloadID) {
		super();
		this.imgThumb = imgThumb;
		this.tittle = tittle;
		this.description = description;

		this.VIDEO_ID = vIDEO_ID;
		this.VIDEO_ID_SD = vIDEO_ID_SD;
		this.VIDEO_ID_LD = vIDEO_ID_LD;
		this.VIDEO_ID_HD = vIDEO_ID_HD;
		this.VIDEO_ID_FHD = vIDEO_ID_FHD;

		this.size = size;
		this.size_ld = size_ld;
		this.size_sd = size_sd;
		this.size_hd = size_hd;
		this.size_fhd = size_fhd;
		this.duration = duration;
		//this.source = source;
		this.downloadID=downloadID;
		
	}
	
	

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the imgThumb
	 */
	public String getImgThumb() {
		return imgThumb;
	}



	/**
	 * @param imgThumb the imgThumb to set
	 */
	public void setImgThumb(String imgThumb) {
		this.imgThumb = imgThumb;
	}



	public Double getSize() {
		return size;
	}
	public void setSize(Double size) {
		this.size = size;
	}

	public Double getSizeLD() {
		return size_ld;
	}
	public void setSizeLD(Double sizeLD) {
		this.size_ld = sizeLD;
	}
	
	public Double getSizeSD() {
		return size_sd;
	}
	public void setSizeSD(Double sizeSD) {
		this.size_sd = sizeSD;
	}

	public Double getSizeHD() {
		return size_hd;
	}
	public void setSizeHD(Double sizeHD) {
		this.size_hd = sizeHD;
	}

	public Double getSizeFHD() {
		return size_fhd;
	}
	public void setSizeFHD(Double sizeFHD) {
		this.size_fhd = sizeFHD;
	}

	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}

	

}
