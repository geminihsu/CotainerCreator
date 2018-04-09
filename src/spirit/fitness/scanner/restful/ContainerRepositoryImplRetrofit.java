package spirit.fitness.scanner.restful;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import spirit.fitness.scanner.common.Constrant;
import spirit.fitness.scanner.common.HttpRequestCode;
import spirit.fitness.scanner.model.Containerbean;
import spirit.fitness.scanner.restful.callback.ContainerCallback;
import spirit.fitness.scanner.restful.listener.ContainerCallBackFunction;

public class ContainerRepositoryImplRetrofit {

	// retrieve return code number
	private ContainerCallBackFunction containerServiceCallBackFunction;

	public void setContainerServiceCallBackFunction(ContainerCallBackFunction _containerServiceCallBackFunction) {
		containerServiceCallBackFunction = _containerServiceCallBackFunction;

	}

	public List<Containerbean> createItem(List<Containerbean> items) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl)
				.addConverterFactory(GsonConverterFactory.create()).build();
		ContainerCallback service = retrofit.create(ContainerCallback.class);
		Response<List<Containerbean>> request = service.createItem(items).execute();
		int code = request.code();

		List<Containerbean> resultData = null;

		if (code == HttpRequestCode.HTTP_REQUEST_OK)
			resultData = request.body();

		if (containerServiceCallBackFunction != null)
			containerServiceCallBackFunction.addContainerInfo(resultData);

		return resultData;

	}

	public List<Containerbean> getAllItems() throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl)
				.addConverterFactory(GsonConverterFactory.create()).build();
		ContainerCallback service = retrofit.create(ContainerCallback.class);
		Response<List<Containerbean>> request = service.getAllItems().execute();
		int code = request.code();

		List<Containerbean> result = retriveCode(code, request);

		return result;
	}

	public List<Containerbean> getItemsByDate(String date) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl)
				.addConverterFactory(GsonConverterFactory.create()).build();
		ContainerCallback service = retrofit.create(ContainerCallback.class);
		Call<List<Containerbean>> items = service.getItemsByDate(date);
		// System.out.println(items.execute().toString());
		return items.execute().body();
	}

	public List<Containerbean> getItemsByContainerNo(String containerNo) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl)
				.addConverterFactory(GsonConverterFactory.create()).build();
		ContainerCallback service = retrofit.create(ContainerCallback.class);

		Call<List<Containerbean>> items = service.getItemsBycontainerNo(containerNo);
		// System.out.println(items.execute().toString());
		return items.execute().body();
	}

	public List<Containerbean> updateItem(List<Containerbean> item) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl)
				.addConverterFactory(GsonConverterFactory.create()).build();
		ContainerCallback service = retrofit.create(ContainerCallback.class);

		Response<List<Containerbean>> request = service.updateItem(item).execute();
		int code = request.code();

		List<Containerbean> result = retriveCode(code, request);
		return result;
	}

	public void deleteItem(Integer Seq){
		Retrofit retrofit = new Retrofit.Builder().baseUrl(Constrant.webUrl)
				.addConverterFactory(GsonConverterFactory.create()).build();
		ContainerCallback service = retrofit.create(ContainerCallback.class);
		
		try {
		Response<Containerbean> request = service.deleteItem(Seq).execute();

		int code = request.code();

		}catch(Exception e) 
		{
			if (containerServiceCallBackFunction != null) {
				containerServiceCallBackFunction.deleteContainerIteam(true);
			}
		}
		//List<Containerbean> result = retriveCode(code, request);
		
		
		// Containerbean result = retriveCode(code,request);
	}

	private List<Containerbean> retriveCode(int code, Response<List<Containerbean>> request) {
		List<Containerbean> resultData = null;

		if (containerServiceCallBackFunction != null) {
			containerServiceCallBackFunction.resultCode(code);
			if (code == HttpRequestCode.HTTP_REQUEST_OK) {
				resultData = request.body();
				containerServiceCallBackFunction.getContainerItems(resultData);
			}
		}
		return resultData;
	}
}
