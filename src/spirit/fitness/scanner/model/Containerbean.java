package spirit.fitness.scanner.model;

import com.google.gson.annotations.SerializedName;

public class Containerbean {
	
	@SerializedName("Seq")
	public Integer Seq;
	@SerializedName("Date")
	public String date;
	@SerializedName("ContainerNo")
	public String ContainerNo;
	@SerializedName("SNBegin")
	public String SNBegin;
	@SerializedName("SNEnd")
	public String SNEnd;
    @SerializedName("Close")
    public Boolean Close;
}
