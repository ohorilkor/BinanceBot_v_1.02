package com.company;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.domain.market.TickerStatistics;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

public class Volume_calculation {

    public static boolean Volume_cal(SendMessage message) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("", "");
        BinanceApiRestClient client = factory.newRestClient();

        List<Candlestick> candlesticks, top_candlestick = null;
        List<TickerStatistics> all24Statistics = client.getAll24HrPriceStatistics();
        TickerStatistics top_elem = new TickerStatistics();

        double top_proz = 0;

        for (int i = 0; i < all24Statistics.size(); i++) {
            if (all24Statistics.get(i).getSymbol().endsWith("USDT")) {
                candlesticks = client.getCandlestickBars(all24Statistics.get(i).getSymbol(), CandlestickInterval.ONE_MINUTE, 2, (Long) null, (Long) null);
                double volDifference = Double.parseDouble(candlesticks.get(0).getVolume()) / Double.parseDouble(all24Statistics.get(i).getVolume());
                if (top_proz < volDifference && !Double.isInfinite(volDifference) && Double.parseDouble(all24Statistics.get(i).getLastPrice()) > Double.parseDouble(candlesticks.get(0).getClose())) {
                    top_proz = volDifference;
                    top_elem = all24Statistics.get(i);
                    top_candlestick = candlesticks;
                }
             //  System.out.println(all24Statistics.get(i).getSymbol() + " current Volume = " + (volDifference) * 100 + "%\n" + "Last Price = " + all24Statistics.get(i).getLastPrice() + "\nOld Price = " + candlesticks.get(0).getClose());
            }
        }
       /* System.out.println("===================================================================");
        System.out.println(top_elem.getSymbol() + " Top Volume = " + top_proz * 100 + "%");
        System.out.println("===================================================================");
        System.out.println("===================================================================");
*/
        if((top_proz * 100) > 10.0){
            message.setText("Top Crypto = " + top_elem.getSymbol() + "\nVolume change = " + top_proz * 100 + "%\n"
                            + "Old Price = "+ top_candlestick.get(0).getClose() + "\nCurrent Price = " + top_elem.getLastPrice());
            return true;
        }
        return false;
    }
}