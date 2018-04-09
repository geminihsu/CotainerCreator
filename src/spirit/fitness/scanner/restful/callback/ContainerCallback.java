package spirit.fitness.scanner.restful.callback;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import spirit.fitness.scanner.model.Containerbean;



public interface ContainerCallback {
	@Headers("Content-Type: application/json")
	
	@GET("FGService/api/Containers")
	Call<List<Containerbean>> getAllItems();
	
	@GET("FGService/api/Containers/date/{date}")
	Call<List<Containerbean>> getItemsByDate(@Path("date") String date);
	
	@GET("FGService/api/Containers/{containerNo}")
	Call<List<Containerbean>> getItemsBycontainerNo(@Path("containerNo") String containerNo);

	//Post ArrayList items
	@POST("FGService/api/Containers")
	Call<List<Containerbean>> createItem(@Body List<Containerbean> itembean);

	
	//PUT more than one item
    @PUT("FGService/api/Containers")
	Call<List<Containerbean>> updateItem(@Body List<Containerbean> itembean);

	//Delete no work
	@DELETE("FGService/api/Containers/{Seq}")
	Call<Containerbean> deleteItem(@Path("Seq") Integer Seq);
}
