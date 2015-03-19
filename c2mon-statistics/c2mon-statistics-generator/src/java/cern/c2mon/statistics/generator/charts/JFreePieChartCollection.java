package cern.c2mon.statistics.generator.charts;

import java.sql.SQLException;
import java.util.List;

import cern.c2mon.statistics.generator.values.IChartCollectionValue;

public class JFreePieChartCollection extends JFreeBarChartCollection {
    /**
     * Set the chart class at initialisation.
     */
    public JFreePieChartCollection() {
        super();
        chartClass = JFreePieChart.class;
    }


    @Override
    public List<IChartCollectionValue> getValuesFromDatabase(String tableName) throws SQLException {
        //retrieve the chart values from the database
        return mapper.getPieChartCollectionData(tableName);
    }

}
