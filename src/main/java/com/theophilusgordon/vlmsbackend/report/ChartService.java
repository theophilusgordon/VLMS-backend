package com.theophilusgordon.vlmsbackend.report;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ChartService {

    public byte[] createChart() throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1, "Series1", "Category1");
        dataset.addValue(4, "Series1", "Category2");
        dataset.addValue(3, "Series1", "Category3");
        dataset.addValue(5, "Series1", "Category4");

        JFreeChart chart = ChartFactory.createBarChart(
                "Sample Chart",
                "Category",
                "Value",
                dataset
        );

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(byteArrayOutputStream, chart, 600, 400);
        return byteArrayOutputStream.toByteArray();
    }
}
