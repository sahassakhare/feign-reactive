package reactivefeign.publisher;


import reactivefeign.client.ReactiveHttpClient;
import reactivefeign.client.ReactiveHttpRequest;
import reactivefeign.client.ReactiveHttpResponse;
import reactor.core.publisher.Mono;

/**
 * Wraps {@link PublisherHttpClient}
 *
 * @author Sergii Karpenko
 */
public class MonoPublisherHttpClient implements PublisherHttpClient {

	private final ReactiveHttpClient reactiveHttpClient;

	public MonoPublisherHttpClient(ReactiveHttpClient reactiveHttpClient) {
		this.reactiveHttpClient = reactiveHttpClient;
	}

	@Override
	public Mono<Object> executeRequest(ReactiveHttpRequest request) {
		Mono<ReactiveHttpResponse> response = Mono.defer(() -> reactiveHttpClient.executeRequest(request));
		return response.flatMap(resp -> Mono.from(resp.body()));
	}
}
