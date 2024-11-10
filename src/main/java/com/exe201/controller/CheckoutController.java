package com.exe201.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.util.Date;

@Controller
public class CheckoutController {
    private final PayOS payOS;

    public CheckoutController(PayOS payOS) {
        super();
        this.payOS = payOS;
    }

    @RequestMapping(value = "/premium-subscription")
    public String Index() {
        return "subscribe";
    }

    @RequestMapping(value = "/payment-success")
    public String Success() {
        return "payment-success";
    }

    @RequestMapping(value = "/payment-cancel")
    public String Cancel() {
        return "payment-cancel";
    }

    // Handle payment link creation for subscription
    @RequestMapping(method = RequestMethod.POST, value = "/create-payment-link", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void checkout(@RequestParam("choice") String choice, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        try {
            final String baseUrl = getBaseUrl(request);
            final String description = "Thanh toan don hang";
            final String returnUrl = baseUrl + "/payment-success";
            final String cancelUrl = baseUrl + "/payment-cancel";

            // Determine the price based on choice
            int price;
            String productName;
            switch (choice) {
                case "month":
                    price = 48000;
                    productName = "Monthly Subscription Plan";
                    break;
                case "year":
                    price = 480000;
                    productName = "Yearly Subscription Plan";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid subscription choice.");
            }

            // Generate order code
            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

            // Create item and payment data
            ItemData item = ItemData.builder().name(productName).quantity(1).price(price).build();
            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount(price)
                    .description(description)
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .item(item)
                    .build();

            // Create payment link
            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            String checkoutUrl = data.getCheckoutUrl();

            // Redirect to the payment link
            httpServletResponse.setHeader("Location", checkoutUrl);
            httpServletResponse.setStatus(302);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getBaseUrl(HttpServletRequest request) {
        // Check if it's a production environment
        String baseUrl = "https://cutting-edge-mck5.onrender.com"; // Hardcoded for production
        // Optionally, you can still allow dynamic URLs for local/dev environments
        if (isLocalEnvironment(request)) {
            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int serverPort = request.getServerPort();
            String contextPath = request.getContextPath();

            baseUrl = scheme + "://" + serverName;
            if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
                baseUrl += ":" + serverPort;
            }
            baseUrl += contextPath;
        }
        return baseUrl;
    }

    private boolean isLocalEnvironment(HttpServletRequest request) {
        // Example check for local environments, based on host or other factors
        return request.getServerName().equals("localhost");
    }

}