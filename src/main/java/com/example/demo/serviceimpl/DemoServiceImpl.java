package com.example.demo.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.service.DemoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("DemoServiceImpl")

public class DemoServiceImpl implements DemoService {
	private static ObjectMapper mapper = new ObjectMapper();

	private static WebClient client = WebClient.create("http://localhost:3001");

	@Override
	public Flux<String> similar(Integer productid, boolean mock) {

		//Contact the first endpoint to get list of IDS
		Mono<String> monoListaId = client.get().uri("/product/{productid}/similarids", productid).retrieve()
				.bodyToMono(String.class);
		monoListaId.subscribe();

		//Convert the list of ID into a flux of the responses, and return the flux
		Flux<String> result = convertListaIdToListaObjetos(monoListaId);
		result.subscribe();
		return result;
	}

	Flux<String> convertListaIdToListaObjetos(Mono<String> mono) {
		return mono.flatMapMany(s -> {
			//Convierto la string recibida a lista de integers
			List<Integer> listaId = new ArrayList<>();
			try {
				listaId = mapper.readValue(s, new TypeReference<List<Integer>>() {
				});
			} catch (Exception e) {
				return Flux.fromIterable(new ArrayList<>());
			}

			
			//Para cada id, hago una consulta que devuelve mono, y acabo juntandolo todo en un flux
			List<Mono<String>> listaMono = new ArrayList<>();
			for (Integer i : listaId) {
				Mono<String> m = client.get().uri("/product/{i}", i).retrieve().bodyToMono(String.class);
				m.subscribe();
				listaMono.add(m);
			}

			return Flux.merge(listaMono);
		});
	}

}
