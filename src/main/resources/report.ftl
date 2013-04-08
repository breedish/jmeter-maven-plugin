<!DOCTYPE html>
<html lang="en">
    <head>
        <title>System Tests Execution Report ${result.startDate?datetime}</title>
        <link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.min.css" rel="stylesheet">
        <script src="http://cdn.jsdelivr.net/jquery/1.9.1/jquery-1.9.1.min.js"></script>
        <script src="http://cdn.jsdelivr.net/tablesorter/2.0.5b/jquery.tablesorter.min.js"></script>
        <script src="http://cdn.jsdelivr.net/bootstrap/2.3.1/js/bootstrap.js"></script>
        <script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
        <!--<script src="http://cdn.jsdelivr.net/bootstrap/2.3.1/js/bootstrap-modal.js"></script>-->
    </head>
    <body>
        <style>
            article,aside,details,figcaption,figure,footer,header,hgroup,main,nav,section,summary{display:block}audio,canvas,video{display:inline-block}audio:not([controls]){display:none;height:0}[hidden]{display:none}html{background:#fff;color:#000;font-family:sans-serif;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}body{margin:0}a:focus{outline:thin dotted}a:active,a:hover{outline:0}h1{font-size:2em;margin:.67em 0}abbr[title]{border-bottom:1px dotted}b,strong{font-weight:bold}dfn{font-style:italic}hr{-moz-box-sizing:content-box;box-sizing:content-box;height:0}mark{background:#ff0;color:#000}code,kbd,pre,samp{font-family:monospace,serif;font-size:1em}pre{white-space:pre-wrap}q{quotes:"\201C""\201D""\2018""\2019"}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sup{top:-0.5em}sub{bottom:-0.25em}img{border:0}svg:not(:root){overflow:hidden}figure{margin:0}fieldset{border:1px solid #c0c0c0;margin:0 2px;padding:.35em .625em .75em}legend{border:0;padding:0}button,input,select,textarea{font-family:inherit;font-size:100%;margin:0}button,input{line-height:normal}button,select{text-transform:none}button,html input[type="button"],input[type="reset"],input[type="submit"]{-webkit-appearance:button;cursor:pointer}button[disabled],html input[disabled]{cursor:default}input[type="checkbox"],input[type="radio"]{box-sizing:border-box;padding:0}input[type="search"]{-webkit-appearance:textfield;-moz-box-sizing:content-box;-webkit-box-sizing:content-box;box-sizing:content-box}input[type="search"]::-webkit-search-cancel-button,input[type="search"]::-webkit-search-decoration{-webkit-appearance:none}button::-moz-focus-inner,input::-moz-focus-inner{border:0;padding:0}textarea{overflow:auto;vertical-align:top}table{border-collapse:collapse;border-spacing:0}
        </style>
        <style>
            * {
                font-size: 10pt;
                padding: 0px;
                margin: 0px;
            }

            pre.prettyprint, code, pre {
                font-size: 8pt;
                padding:0;
                line-height: 14px;
            }

            code span, pre span {
                padding:0px.
            ;
                margin: 0px
            }

            .table th, .table td {
                padding: 4px;
            }

            table.with-auto-width {
            width: auto
            }

            .with-padding-bottom {
            padding-bottom:70px;
            }

            table.table-bordered {
            border-collapse: collapse;
            }


            /**
            *  Sort order on column
            **/
            th.headerSortUp:before {
            content:"\25BC";
            font-size:10pt;
            color:#999;
            }

            th.headerSortDown:before {
            content:"\25B2";
            font-size:10pt;
            color:#999;
            }

            .table thead tr th,
            .thead tr th {
            background-color: #eee;
            }
        </style>
        <div class="row-fluid">
            <div class="navbar navbar-fixed-top">
                <div class="navbar-inner">
                    <div class="container">
                        <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </a>
                        <a class="brand" href="#">System Tests Execution Report ${result.startDate?datetime}<span style="position: relative;top: -10px;" class="label <#if result.success>label-success">PASSED<#else>label-important">FAILED</span></#if></a>
                    </div>
                </div>
            </div>
            <div class="row" style="padding:50px;margin-left:0px">
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
                                    <a href="#${test.test.name?replace(".","_")}" data-toggle="modal">${test.test.name}</a>
                                </td>
                                <td>${test.executionTime}</td>
                                <td>${test.explain.samples?size}</td>
                                <td><#if test.success><span class="label label-success">PASSED</span><#else><span class="label label-important">FAILED</span></#if></td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
                <section class="row" id="detailed-results" style="padding: 0 40px">

                    <#list result.executionResults as test>
                        <#if !test.success>
                        <div id="${test.test.name?replace(".","_")}" class="modal hide fade" style="width:90%;top:5%;margin-left:0px;left:5%;" tabindex="-1" role="dialog" aria-labelledby="modal_header_${test.test.name?replace(".","_")}_${test_index}" aria-hidden="true">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                <h3 id="modal_header_${test.test.name?replace(".","_")}_${test_index}"><b>${test.test.name}</b> Details</h3>
                            </div>
                            <div class="modal-body" style="max-height:600px">
                                <ul class="nav nav-tabs nav-stacked">
                                <#list test.explain.samples as sample>
                                    <li>
                                        <a data-toggle="collapse" href="#explain_call_detailed_${test.test.name?replace(".","_")}_${sample_index+1}" >${sample_index+1}. ${sample.label} <#if sample.success><span class="label label-success">PASSED</span><#else><span class="label label-important">FAILED</span></#if></a>
                                        <div class="in" style="padding-left:50px" id="explain_call_detailed_${test.test.name?replace(".","_")}_${sample_index+1}">
                                            <ul class="nav nav-tabs">
                                                <li><a href="#explain_call_detailed_${test.test.name?replace(".","_")}_${sample_index+1}_request" data-toggle="tab">Request</a></li>
                                                <li><a href="#explain_call_detailed_${test.test.name?replace(".","_")}_${sample_index+1}_response" data-toggle="tab">Response</a></li>
                                            </ul>
                                            <div class="tab-content">
                                                <div class="tab-pane active" id="explain_call_detailed_${test.test.name?replace(".","_")}_${sample_index+1}_request">
                                                    <dl class="dl-horizontal">
                                                        <dt>Request URL:</dt>
                                                        <dd>${sample.url}</dd>
                                                        <dt>Request Method:</dt>
                                                        <dd>${sample.method}</dd>
                                                        <dt>Request Headers:</dt>
                                                        <dd>
                                                            <#if sample.requestHeaders?has_content>
                                                                <pre class="prettyprint" style="white-space: pre;">
                                                                    <br />${sample.requestHeaders!}
                                                                </pre>
                                                            </#if>
                                                        </dd>
                                                        <dt></dt>
                                                    </dl>

                                                    <#if sample.queryString?has_content>
                                                        <b>Request Query String:</b>
                                                        <pre class="prettyprint" style="white-space: pre;">
                                                            <br />${sample.queryString?html}
                                                        </pre>
                                                    </#if>
                                                </div>
                                                <div class="tab-pane" id="explain_call_detailed_${test.test.name?replace(".","_")}_${sample_index+1}_response">
                                                    <dl class="dl-horizontal">
                                                        <dt>Response Status Code:</dt>
                                                        <dd>${sample.responseCode} / ${sample.responseMessage}</dd>
                                                    </dl>

                                                    Response Headers:
                                                    <pre class="prettyprint" style="white-space: pre;">
                                                        <br />${sample.responseHeaders!}
                                                    </pre>
                                                    Response:
                                                    <pre class="prettyprint" style="white-space: pre;">
                                                        <br />${sample.responseData?html}
                                                    </pre>

                                                    Assertions:
                                                    <#if sample.assertions?has_content>
                                                        <table class="table table-bordered table-striped table-condensed">
                                                            <thead>
                                                            <tr>
                                                                <th>Assertion</th>
                                                                <th>Failure</th>
                                                                <th>Error</th>
                                                            </tr>

                                                            </thead>
                                                            <tbody>
                                                            <#list sample.assertions as assertion>
                                                                <tr>
                                                                    <td>
                                                                       ${assertion.name}
                                                                    </td>
                                                                    <td>
                                                                        <#if assertion.failure><span class="label label-important">FAILURE</span><#else><span class="label label-success">PASSED</span></#if>
                                                                    </td>
                                                                    <td>
                                                                        <#if assertion.error><span class="label label-important">ERROR</span><#else><span class="label label-success">PASSED</span></#if>
                                                                    </td>
                                                                </tr>
                                                            </#list>
                                                            </tbody>
                                                        </table>
                                                    </#if>
                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                </#list>
                                </ul>

                            </div>
                            <div class="modal-footer">
                                <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
                            </div>
                        </div>
                        </#if>
                    </#list>
                </section>
            </div>
        </div>
        <script>
            $(function() {
                $(".tablesorter").tablesorter();
                //$(".collapse").collapse();
            })
        </script>

    </body>
</html>
