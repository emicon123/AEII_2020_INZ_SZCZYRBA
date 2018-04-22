package core;

import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsBuilder;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = StatisticsBuilder.class)
public class DbscanBuilder implements StatisticsBuilder {
    @Override
    public String getName() {
        return "DBSCAN";
    }

    @Override
    public Statistics getStatistics() {
        return new Dbscan();
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return Dbscan.class;
    }
}
