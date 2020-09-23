<h1>Spring Batch</h1>
<ol>
    <li><b>BatchConfig.Job</b>: The Job Bean to run.</li>
    <li><b>BatchConfig.Step</b>: The Step Bean to run. A job consists of one (start) or more (.flow(...).next(...)) Steps.</li>
    <li><b>BatchConfig.FlatFileItemReader</b>: The ItemReader Bean that the step will use.</li>
    <li><b>BatchConfig.LineMapper</b>: The LineMapper Bean that the FlatFileItemReader will use to convert line into POJO.</li>
    <li><b>Processor</b>: The ItemProcessor Component.</li>
    <li><b>Writer</b>: The ItemWriter Component.</li>
    <li><b>BatchController.JobLauncher</b>: Bean to manage Jobs (eg: start).</li>
</ol>