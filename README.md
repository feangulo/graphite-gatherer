#Gatherer

The Gatherer application will help you query for information in Graphite programatically. You can define any metrics that you would like to query on and do so in a regular basis.

The default metrics are the following:

```
Monitor Interprocess Communication
   - I1 = sumSeries(*.relays.*.metricsReceived)
   - I2 = sumSeries(*.relays.*.destinations.*.sent)
   - I3 = sumSeries(*.aggregator.*.metricsReceived)
   - I4 = sumSeries(*.aggregator.*.aggregatedDatapointsSent)
   
Monitor Cache Behavior
   - C1 = sumSeries(*.agents.*.metricsReceived)
   - C2 = sumSeries(*.agents.*.creates)
   - C3 = sumSeries(*.agents.*.updateOperations)
   - C4 = averageSeries(*.agents.*.pointsPerUpdate)
   - C5 = sumSeries(*.agents.*.committedPoints)
   
Monitor Queue Backlog
   - Q1 = maxSeries(*.agents.*.cache.size)
   - Q2 = maxSeries(*.agents.*.cache.queues)
   - Q3 = maxSeries(*.aggregator.*.bufferedDatapoints)
   - Q4 = maxSeries(*.relays.*.destinations.*.relayMaxQueueLength)
   
Monitor Load Balancing
   - LA = sumSeries(*.agents.*.metricsReceived)
   - L1 = sumSeries(*.agents.graphite1.metricsReceived)
   - L2 = sumSeries(*.agents.graphite2.metricsReceived)
   
Monitor Cache Querying
   - RA = sumSeries(*.agents.*.cache.queries)
   - R1 = sumSeries(*.agents.graphite1.cache.queries)
   - R2 = sumSeries(*.agents.graphite2.cache.queries)
```

You can run the Gatherer using the following command:

```
java -jar gatherer.jar localhost 120 60 60000 10
```

You will see output such as the following:

```
Executing request: http://localhost/render/?target=sumSeries(*.relays.*.metricsReceived)&format=json&from=-120s&until=-60s
Executing request: http://localhost/render/?target=sumSeries(*.relays.*.destinations.*.sent)&format=json&from=-120s&until=-60s
Executing request: http://localhost/render/?target=sumSeries(*.aggregator.*.metricsReceived)&format=json&from=-120s&until=-60s
Executing request: http://localhost/render/?target=sumSeries(*.aggregator.*.aggregateDatapointsSent)&format=json&from=-120s&until=-60s
Executing request: http://localhost/render/?target=sumSeries(*.agents.*.metricsReceived)&format=json&from=-120s&until=-60s
@ 1401999720
	- I1 = 329866.0
	- I2 = 330399.0
	- I5 = 333021.0
Executing request: http://localhost/render/?target=sumSeries(*.agents.*.metricsReceived)&format=json&from=-120s&until=-60s
Executing request: http://localhost/render/?target=sumSeries(*.agents.*.creates)&format=json&from=-120s&until=-60s
Executing request: http://localhost/render/?target=sumSeries(*.agents.*.updateOperations)&format=json&from=-120s&until=-60s
Executing request: http://localhost/render/?target=averageSeries(*.agents.*.pointsPerUpdate)&format=json&from=-120s&until=-60s
Executing request: http://localhost/render/?target=sumSeries(*.agents.*.committedPoints)&format=json&from=-120s&until=-60s
@ 1401999720
	- C1 = 333021.0
	- C2 = 0.0
	- C3 = 90548.0
	- C4 = 3.7170355644195907
	- C5 = 336572.0
Executing request: http://localhost/render/?target=maxSeries(*.agents.*.cache.size)&format=json&from=-120s&until=-60s
Executing request: http://localhost/render/?target=maxSeries(*.agents.*.cache.queues)&format=json&from=-120s&until=-60s
Executing request: http://localhost/render/?target=maxSeries(*.aggregator.*.bufferedDatapoints)&format=json&from=-120s&until=-60s
Executing request: http://localhost/render/?target=maxSeries(*.relays.*.destinations.*.relayMaxQueueLength)&format=json&from=-120s&until=-60s
@ 1401999720
	- Q1 = 14296.0
	- Q2 = 5760.0
	- Q4 = 85.0
Executing request: http://localhost/render/?target=sumSeries(*.agents.*.metricsReceived)&format=json&from=-120s&until=-60s
Executing request: http://localhost/render/?target=sumSeries(*.agents.graphite1.metricsReceived)&format=json&from=-120s&until=-60s
Executing request: http://localhost/render/?target=sumSeries(*.agents.graphite2.metricsReceived)&format=json&from=-120s&until=-60s
@ 1401999720
	- LA = 333021.0
Executing request: http://localhost/render/?target=sumSeries(*.agents.*.cache.queries)&format=json&from=-120s&until=-60s
Executing request: http://localhost/render/?target=sumSeries(*.agents.graphite1.cache.queries)&format=json&from=-120s&until=-60s
Executing request: http://localhost/render/?target=sumSeries(*.agents.graphite2.cache.queries)&format=json&from=-120s&until=-60s
@ 1401999720
	- RA = 0.0
```
