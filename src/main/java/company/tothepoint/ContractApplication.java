package company.tothepoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ContractApplication {
	private static final String CONTRACT_QUEUE = "contract-queue";
	private static final String BUSINESSUNIT_EXCHANGE = "businessunit-exchange";
	private static final String BUSINESSUNIT_ROUTING = "businessunit-routing";
	private static final String BEDIENDE_EXCHANGE = "bediende-exchange";
	private static final String BEDIENDE_ROUTING = "bediende-routing";
	private static final String CONTRACT_EXCHANGE = "contract-exchange";
	private static final String CONTRACT_ROUTING = "contract-routing";

	public static void main(String[] args) {
		SpringApplication.run(ContractApplication.class, args);
	}

	@Bean
	String queueName() {
		return CONTRACT_QUEUE;
	}

	@Bean
	Queue queue() {
		return new Queue(CONTRACT_QUEUE, true, false, false);
	}

	@Bean
	Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
		Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
		jackson2JsonMessageConverter.setJsonObjectMapper(objectMapper);
		return jackson2JsonMessageConverter;
	}

	@Bean
	TopicExchange businessUnitTopicExchange() {
		return new TopicExchange(BUSINESSUNIT_EXCHANGE, true, false);
	}

	@Bean
	TopicExchange bediendeTopicExchange() {
		return new TopicExchange(BEDIENDE_EXCHANGE, true, false);
	}

	@Bean
	TopicExchange contractTopicExchange() {
		return new TopicExchange(CONTRACT_EXCHANGE, true, false);
	}

	@Bean
	Binding businessUnitBinding(Queue queue, TopicExchange businessUnitTopicExchange) {
		return BindingBuilder.bind(queue).to(businessUnitTopicExchange).with(BUSINESSUNIT_ROUTING);
	}

	@Bean
	Binding bediendeBinding(Queue queue, TopicExchange bediendeTopicExchange) {
		return BindingBuilder.bind(queue).to(bediendeTopicExchange).with(BEDIENDE_ROUTING);
	}

	@Bean
	Binding contractBinding(Queue queue, TopicExchange contractTopicExchange) {
		return BindingBuilder.bind(queue).to(contractTopicExchange).with(CONTRACT_ROUTING);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jackson2JsonMessageConverter, Receiver receiver) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(CONTRACT_QUEUE);
		container.setMessageConverter(jackson2JsonMessageConverter);
		container.setMessageListener(receiver);
		return container;
	}

	@Bean
	Receiver receiver() {
		return new Receiver();
	}
}
