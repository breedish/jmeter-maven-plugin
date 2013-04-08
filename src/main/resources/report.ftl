<!DOCTYPE html>
<html lang="en">
    <head>
        <title>System Tests Execution Report ${result.startDate?datetime}</title>
        <link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.min.css" rel="stylesheet">
        <script src="http://cdn.jsdelivr.net/jquery/1.9.1/jquery-1.9.1.min.js"></script>
        <script src="http://cdn.jsdelivr.net/tablesorter/2.0.5b/jquery.tablesorter.min.js"></script>
        <script src="http://cdn.jsdelivr.net/bootstrap/2.3.1/js/bootstrap.min.js"></script>
        <script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
        <!--<script src="http://cdn.jsdelivr.net/bootstrap/2.3.1/js/bootstrap-modal.js"></script>-->
    </head>
    <body>
        <div class="row-fluid">
            <div class="span2">&nbsp;</div>
            <div class="span10">
                <h1>System Tests Execution Report ${result.startDate?datetime}.</h1>
            </div>
            <div class="row-fluid">
                <table class="table table-bordered table-stripped tablesorter">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>System Test</th>
                            <th>Execution Time</th>
                            <th>Calls</th>
                            <th>Passed</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#list result.executionResults as test>
                            <tr>
                                <td>${test_index+1}</td>
                                <td>
                                    <a href="#${test.test.name}" data-toggle="modal">${test.test.name}</a>
                                </td>
                                <td>${test.executionTime}</td>
                                <td>${test.explain.samples?size}</td>
                                <td>${test.success?string("PASSED","FAILED")}</td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
                <section class="row" id="detailed-results" style="padding: 0 40px">
                    <#list result.executionResults as test>
                        <div id="${test.test.name}" class="modal hide fade" style="width:90%;margin-left:0px;left:5%;" tabindex="-1" role="dialog" aria-labelledby="modal_header_${test.test.name}_${test_index}" aria-hidden="true">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                <h3 id="modal_header_${test.test.name}_${test_index}"><b>${test.test.name}</b> Details</h3>
                            </div>
                            <div class="modal-body" style="height:1000px;overflow:scroll">
                                hhhhhh
                                <div class="accordion collapse" id="details_${test.test.name}">
                                    <#list test.explain.samples as sample>
                                    <div class="accordion-group">
                                        <div class="accordion-heading">
                                            <a class="accordion-toggle" data-toggle="collapse" data-parent="#explain_call_${test.test.name}_${sample.label}" href="#explain_call_detailed_${test.test.name}_${sample_index+1}">
                                                ${sample_index+1}. ${sample.label}
                                            </a>
                                        </div>
                                        <div id="explain_call_detailed_${test.test.name}_${sample_index+1}" class="accordion-body collapse in">
                                            <div class="accordion-inner">
                                                <code class="prettyprint">
                                                    ${sample.responseData}
                                                </code>
                                            </div>
                                        </div>
                                    </div>
                                </#list>
                            </div>
                            </div>
                            <!--<div class="modal-footer">-->
                                <!--<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>-->
                            <!--</div>-->
                        </div>
                    </#list>
                </section>
            </div>
        </div>
        <script>
            $(function() {
                $(".tablesorter").tablesorter();
                $(".collapse").collapse();
            })
        </script>

    </body>
</html>
