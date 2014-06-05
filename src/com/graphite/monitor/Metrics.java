package com.graphite.monitor;

public class Metrics {

	/* Monitor Interprocess Communication */
	public static final String I1 = "sumSeries(*.relays.*.metricsReceived)";
	public static final String I2 = "sumSeries(*.relays.*.destinations.*.sent)";
	public static final String I3 = "sumSeries(*.aggregator.*.metricsReceived)";
	public static final String I4 = "sumSeries(*.aggregator.*.aggregateDatapointsSent)";
	public static final String I5 = "sumSeries(*.agents.*.metricsReceived)";
	
	/* Monitor Cache Behavior */
	public static final String C1 = "sumSeries(*.agents.*.metricsReceived)";
	public static final String C2 = "sumSeries(*.agents.*.creates)";
	public static final String C3 = "sumSeries(*.agents.*.updateOperations)";
	public static final String C4 = "averageSeries(*.agents.*.pointsPerUpdate)";
	public static final String C5 = "sumSeries(*.agents.*.committedPoints)";
	
	/* Monitor Queue Backlog */
	public static final String Q1 = "maxSeries(*.agents.*.cache.size)";
	public static final String Q2 = "maxSeries(*.agents.*.cache.queues)";
	public static final String Q3 = "maxSeries(*.aggregator.*.bufferedDatapoints)";
	public static final String Q4 = "maxSeries(*.relays.*.destinations.*.relayMaxQueueLength)";
	
	/* Monitor Load Balancing */
	public static final String LA = "sumSeries(*.agents.*.metricsReceived)";
	public static final String L4 = "sumSeries(*.agents.graphite004*.metricsReceived)";
	public static final String L5 = "sumSeries(*.agents.graphite005*.metricsReceived)";
	public static final String L6 = "sumSeries(*.agents.graphite006*.metricsReceived)";
	
	/* Monitor Cache Querying */
	public static final String RA = "sumSeries(*.agents.*.cache.queries)";
	public static final String R4 = "sumSeries(*.agents.graphite4*.cache.queries)";
	public static final String R5 = "sumSeries(*.agents.graphite5*.cache.queries)";
	public static final String R6 = "sumSeries(*.agents.graphite6*.cache.queries)";	
	
}
