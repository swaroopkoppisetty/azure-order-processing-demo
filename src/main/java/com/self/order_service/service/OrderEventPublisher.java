package com.self.order_service.service;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {

    private final ServiceBusSenderClient senderClient;
    private final boolean enabled;

    public OrderEventPublisher(
            @Value("${azure.servicebus.namespace}") String namespace,
            @Value("${azure.servicebus.topic}") String topic,
            @Value("${azure.servicebus.enabled:false}") boolean enabled) {

        this.enabled = enabled;

        if (enabled) {
            this.senderClient = new ServiceBusClientBuilder()
                    .fullyQualifiedNamespace(namespace)
                    .credential(new DefaultAzureCredentialBuilder().build())
                    .sender()
                    .topicName(topic)
                    .buildClient();
        } else {
            this.senderClient = null;
        }
    }

    public void publishOrderCreated(Long orderId, String customerName,
                                    String productName, double amount) {

        if (!enabled) {
            return;
        }

        String message = """
            {
              "eventType": "OrderCreated",
              "orderId": %d,
              "customerName": "%s",
              "productName": "%s",
              "amount": %.2f
            }
            """.formatted(orderId, customerName, productName, amount);

        senderClient.sendMessage(
                new com.azure.messaging.servicebus.ServiceBusMessage(message)
        );
    }
}
