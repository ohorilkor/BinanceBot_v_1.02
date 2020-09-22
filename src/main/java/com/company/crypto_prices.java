package com.company;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;

public class crypto_prices {
    public static String getPrice(String crypto){
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("API-KEY", "SECRET");
        BinanceApiRestClient client = factory.newRestClient();

        TickerStatistics tickerStatistics = client.get24HrPriceStatistics(crypto.toUpperCase());
        return tickerStatistics.getLastPrice();
    }
}
