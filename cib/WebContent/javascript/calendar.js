
    var scwDateNow = new Date(Date.parse(new Date().toDateString()));

    var scwBaseYear        = scwDateNow.getFullYear()-10;

    var scwDropDownYears   = 20;

    var scwLanguage;

    function scwSetLanguage()
        {switch (scwLanguage)
            {case 'br':
                // Brazilian Portuguese (provided by Rafael Pirolla)
                scwToday               = 'Hoje:';
                scwDrag                = 'clique aqui para arrastar';
                scwArrMonthNames       = ['Jan','Fev','Mar','Abr','Mai','Jun',
                                          'Jul','Ago','Set','Out','Nov','Dez'];
                scwArrWeekInits        = ['D','S','T','Q','Q','S','S'];
                scwInvalidDateMsg      = 'A data e invalida.\n';
                scwOutOfRangeMsg       = 'A data esta fora do escopo definido.';
                scwDoesNotExistMsg     = 'A data nao existe.';
                scwInvalidAlert        = ['Data invalida (',') ignorada.'];
                scwDateDisablingError  = ['Erro ',' n&atilde;o &eacute; um objeto Date.'];
                scwRangeDisablingError = ['Erro ',' deveria consistir de dois elementos.'];
                break;
              //add by linrui 20180507
            case 'zh_CN':
                scwToday               = '\u4ECA\u65E5:';
                scwDrag                = '\u70B9\u51FB\u62D6\u52A8';
                scwArrMonthNames       = ['\u4E00\u6708','\u4E8C\u6708','\u4E09\u6708','\u56DB\u6708','\u4E94\u6708','\u516D\u6708',
                                          '\u4E03\u6708','\u516B\u6708','\u4E5D\u6708','\u5341\u6708','\u5341\u4E00\u6708','\u5341\u4E8C\u6708'];
                scwArrWeekInits        = ['\u65E5','\u4E00','\u4E8C','\u4E09','\u56DB','\u4E94','\u516D'];
                scwInvalidDateMsg      = '\u65E5\u671F\u65E0\u6548\u3002\n';
                scwOutOfRangeMsg       = '\u65E5\u671F\u8D85\u51FA\u8303\u56F4\u3002';
                scwDoesNotExistMsg     = '\u65E5\u671F\u4E0D\u5B58\u5728\u3002';
                scwInvalidAlert        = ['\u65E0\u6548\u65E5\u671F(',') \u5DF2\u5FFD\u7565\u3002'];
                scwDateDisablingError  = ['\u9519\u8BEF ',' \u4E0D\u662F\u65E5\u671F\u7C7B\u578B\u3002'];
                scwRangeDisablingError = ['\u9519\u8BEF ',' \u65E5\u671F\u5E94\u7531\u4E24\u4E2A\u5143\u7D20\u7EC4\u6210\u3002'];
                break;
            case 'zh_HK':
                scwToday               = '\u4ECA\u65E5:';
                scwDrag                = '\u9EDE\u64CA\u62D6\u52D5';
                scwArrMonthNames       = ['\u4E00\u6708','\u4E8C\u6708','\u4E09\u6708','\u56DB\u6708','\u4E94\u6708','\u516D\u6708',
                                          '\u4E03\u6708','\u516B\u6708','\u4E5D\u6708','\u5341\u6708','\u5341\u4E00\u6708','\u5341\u4E8C\u6708'];
                scwArrWeekInits        = ['\u65E5','\u4E00','\u4E8C','\u4E09','\u56DB','\u4E94','\u516D'];
                scwInvalidDateMsg      = '\u65E5\u671F\u7121\u6548\u3002\n';
                scwOutOfRangeMsg       = '\u65E5\u671F\u8D85\u51FA\u7BC4\u570D\u3002';
                scwDoesNotExistMsg     = '\u65E5\u671F\u4E0D\u5B58\u5728\u3002';
                scwInvalidAlert        = ['\u7121\u6548\u65E5\u671F (',') \u5DF2\u88AB\u5FFD\u7565\u3002'];
                scwDateDisablingError  = ['\u932F\u8AA4 ','\u4E0D\u662F\u65E5\u671F\u985E\u578B\u3002'];
                scwRangeDisablingError = ['\u932F\u8AA4 ','\u65E5\u671F\u61C9\u7531\u5169\u500B\u5143\u7D20\u7D44\u6210\u3002'];
                break;
              

             default:
                // English
                scwToday               = 'Today:';
                scwDrag                = 'click here to drag';
                scwArrMonthNames       = ['Jan','Feb','Mar','Apr','May','Jun',
                                          'Jul','Aug','Sep','Oct','Nov','Dec'];
                scwArrWeekInits        = ['S','M','T','W','T','F','S'];
                scwInvalidDateMsg      = 'The entered date is invalid.\n';
                scwOutOfRangeMsg       = 'The entered date is out of range.';
                scwDoesNotExistMsg     = 'The entered date does not exist.';
                scwInvalidAlert        = ['Invalid date (',') ignored.'];
                scwDateDisablingError  = ['Error ',' is not a Date object.'];
                scwRangeDisablingError = ['Error ',' should consist of two elements.'];
            }
        }

    var scwWeekStart       =    1;

    var scwWeekNumberDisplay    = false;

    var scwWeekNumberBaseDay    = 4;

    var scwArrDelimiters   = ['/','-','.',',',' '];

    var scwDateDisplayFormat = 'DD/MM/YYYY';        // e.g. 'MMM-DD-YYYY' for the US

    var scwDateOutputFormat  = 'DD/MM/YYYY';   // e.g. 'MMM-DD-YYYY' for the US

    var scwDateInputSequence = 'DMY';           // e.g. 'MDY' for the US

    var scwZindex          = 1;

    var scwBlnStrict       = false;

    var scwEnabledDay      = [true, true, true, true, true, true, true,
                              true, true, true, true, true, true, true,
                              true, true, true, true, true, true, true,
                              true, true, true, true, true, true, true,
                              true, true, true, true, true, true, true,
                              true, true, true, true, true, true, true];

    var scwDisabledDates   = new Array();

    var scwActiveToday = true;

    var scwOutOfRangeDisable = true;

    var scwAllowDrag = false;

    var scwClickToHide = false;

    var scwBackground           = '#31669c';    // Calendar background
    var scwHeadText             = '#CCCCCC';    // Colour of week headings

    var scwTodayText            = '#FFFFFF',
        scwTodayHighlight       = '#FF0000';

    var scwHighlightText        = '#000000',
        scwHighlightBackground  = '#FF0000';

    var scwDragText             = '#CCCCFF',
        scwDragBackground       = '#9999CC';

    var scwWeekNumberText       = '#CCCCCC',
        scwWeekNumberBackground = '#776677';

    var scwWeekendText          = '#000000',
        scwWeekendBackground    = '#97CAFB';

    var scwExMonthText          = '#666666',
        scwExMonthBackground    = '#CCCCCC';

    var scwCellText             = '#000000',
        scwCellBackground       = '#87BAFB';

    var scwInDateText           = '#FF0000',
        scwInDateBackground     = '#FFFF66';

    var scwDisabledWeekendText          = '#CC6666',
        scwDisabledWeekendBackground    = '#999999';

    var scwDisabledExMonthText          = '#666666',
        scwDisabledExMonthBackground    = '#999999';

    var scwDisabledCellText             = '#000000',
        scwDisabledCellBackground       = '#999999';

    var scwDisabledInDateText           = '#FF0000',
        scwDisabledInDateBackground     = '#CC9999';

    document.writeln("<style>");
    document.writeln(   '.scw       {padding:1px;vertical-align:middle;}');
    document.writeln(   'iframe.scw {position:absolute;z-index:' + scwZindex   +
                                    ';top:0px;left:0px;visibility:hidden;'     +
                                    'width:1px;height:1px;}');
    document.writeln(   'table.scw  {padding:0px;visibility:hidden;'           +
                                    'position:absolute;width:200px;'           +
                                    'top:0px;left:0px;z-index:' +(scwZindex+1) +
                                    ';text-align:center;cursor:default;'       +
                                    'padding:1px;vertical-align:middle;'       +
                                    'background-color:' + scwBackground        +
                                    ';border:ridge 2px;font-size:10pt;'        +
                                    'font-family:Arial,Helvetica,Sans-Serif;'  +
                                    'font-weight:bold;}');
    document.writeln(   'td.scwDrag {text-align:center;font-size:8pt;' +
                                    'background-color:'  + scwDragBackground +
                                    ';padding:0px 0px;color:' + scwDragText  +
                                    "}");
    document.writeln(   'td.scwHead {padding:0px 0px;text-align:center;}');
    document.writeln(   'select.scwHead {margin:3px 1px;}');
    document.writeln(   'input.scwHead  {height:22px;width:22px;'              +
                                        'vertical-align:middle;'               +
                                        'text-align:center;margin:2px 1px;'    +
                                        'font-size:10pt;font-family:fixedSys;' +
                                        'font-weight:bold;}');
    document.writeln(   'td.scwWeekNumberHead '                                +
                                        '{text-align:center;font-weight:bold;' +
                                        'padding:0px;color:'                   +
                                            scwBackground + ';}');
    document.writeln(   'td.scwWeek     {text-align:center;font-weight:bold;'  +
                                        'padding:0px;color:'                   +
                                            scwHeadText + ';}');
    document.writeln(   'table.scwCells {text-align:right;font-size:8pt;'      +
                                        'width:96%;font-family:'               +
                                        'Arial,Helvetica,Sans-Serif;}');
    document.writeln(   'td.scwCells    {padding:3px;vertical-align:middle;'   +
                                        'width:16px;height:16px;'              +
                                        'font-weight:bold;color:'              +
                                            scwCellText                        +
                                        ';background-color:'                   +
                                            scwCellBackground                  +
                                        '}');
    document.writeln(   'td.scwWeekNo   {padding:3px;vertical-align:middle;'   +
                                        'width:16px;height:16px;'              +
                                        'font-weight:bold;color:'              +
                                            scwWeekNumberText                  +
                                        ';background-color:'                   +
                                            scwWeekNumberBackground            +
                                        '}');
    document.writeln(   'td.scwWeeks {padding:3px;vertical-align:middle;'      +
                                     'width:16px;height:16px;'                 +
                                     'font-weight:bold;color:' + scwCellText   +
                                     ';background-color:' + scwCellBackground  +
                                     '}');
    document.writeln(   'td.scwFoot  {padding:0px;text-align:center;'          +
                                     'font-weight:normal;color:'               +
                                      scwTodayText + ';}');
    document.writeln("</style>");

    var scwTargetEle,
        scwTriggerEle,
        scwMonthSum            = 0,
        scwBlnFullInputDate    = false,
        scwPassEnabledDay      = new Array(),
        scwSeedDate            = new Date(),
        scwParmActiveToday     = true,
        scwWeekStart           = scwWeekStart%7,
        scwToday,
        scwDrag,
        scwArrMonthNames,
        scwArrWeekInits,
        scwInvalidDateMsg,
        scwOutOfRangeMsg,
        scwDoesNotExistMsg,
        scwInvalidAlert,
        scwDateDisablingError,
        scwRangeDisablingError;

    Date.prototype.scwFormat =
        function(scwFormat)
            {var charCount = 0,
                 codeChar  = '',
                 result    = '';

             for (var i=0;i<=scwFormat.length;i++)
                {if (i<scwFormat.length && scwFormat.charAt(i)==codeChar)
                        {
                         charCount++;
                        }
                 else   {switch (codeChar)
                            {case 'y': case 'Y':
                                result += (this.getFullYear()%Math.
                                            pow(10,charCount)).toString().
                                            scwPadLeft(charCount);
                                break;
                             case 'm': case 'M':
                                result += (charCount<3)
                                            ?(this.getMonth()+1).
                                                toString().scwPadLeft(charCount)
                                            :scwArrMonthNames[this.getMonth()];
                                break;
                             case 'd': case 'D':
                                // If we find a D, get the date and format it
                                result += this.getDate().toString().
                                            scwPadLeft(charCount);
                                break;
                             default:
                                // Copy any unrecognised characters across
                                while (charCount-- > 0) {result += codeChar;}
                            }

                         if (i<scwFormat.length)
                            {// Store the character we have just worked on
                             codeChar  = scwFormat.charAt(i);
                             charCount = 1;
                            }
                        }
                }
             return result;
            }

    // Add a method to left pad zeroes

    String.prototype.scwPadLeft =
        function(padToLength)
            {var result = '';
             for (var i=0;i<(padToLength - this.length);i++) {result += '0';}
             return (result + this);
            }

    Function.prototype.runsAfterSCW =
        function()  {var func = this,
                         args = new Array(arguments.length);

                     for (var i=0;i<args.length;++i)
                        {args[i] = arguments[i];}

                     return function()
                        {// concat/join the two argument arrays
                         for (var i=0;i<arguments.length;++i)
                            {args[args.length] = arguments[i];}

                         return (args.shift()==scwTriggerEle)
                                    ?func.apply(this, args):null;
                        }
                    };

    var scwNextActionReturn, scwNextAction;

    // Start of Function Library

    function showCal(scwEle,scwSourceEle)    {scwShow(scwEle,scwSourceEle);}
    function scwShow(scwEle,scwSourceEle , langCode)//mod by linrui 20180507
        {scwTriggerEle = scwSourceEle;

         scwParmActiveToday = true;

         for (var i=0;i<7;i++)
            {scwPassEnabledDay[(i+7-scwWeekStart)%7] = true;
             for (var j=2;j<arguments.length;j++)
                {if (arguments[j]==i)
                    {scwPassEnabledDay[(i+7-scwWeekStart)%7] = false;
                     if (scwDateNow.getDay()==i) scwParmActiveToday = false;
                    }
                }
            }

         scwSeedDate = scwDateNow;

         // Strip space characters from start and end of date input
         scwEle.value = scwEle.value.replace(/^\s+/,'').replace(/\s+$/,'');

         // Set the language-dependent elements
         scwLanguage = langCode;
         scwSetLanguage();

         document.getElementById('scwDragText').innerHTML = scwDrag;

         document.getElementById('scwMonths').options.length = 0;
         for (i=0;i<scwArrMonthNames.length;i++)
            document.getElementById('scwMonths').options[i] =
                new Option(scwArrMonthNames[i],scwArrMonthNames[i]);

         document.getElementById('scwYears').options.length = 0;
         for (i=0;i<scwDropDownYears;i++)
            document.getElementById('scwYears').options[i] =
                new Option((scwBaseYear+i),(scwBaseYear+i));

         for (i=0;i<scwArrWeekInits.length;i++)
            document.getElementById('scwWeekInit' + i).innerHTML =
                          scwArrWeekInits[(i+scwWeekStart)%
                                            scwArrWeekInits.length];

         if (document.getElementById('scwFoot'))
            document.getElementById('scwFoot').innerHTML =
                    scwToday + " " +
                    scwDateNow.scwFormat(scwDateDisplayFormat);

         if (scwEle.value.length==0)
            {
             scwBlnFullInputDate=false;

             if ((new Date(scwBaseYear+scwDropDownYears-1,11,31))<scwSeedDate ||
                 (new Date(scwBaseYear,0,1))                     >scwSeedDate
                )
                {scwSeedDate = new Date(scwBaseYear +
                                        Math.floor(scwDropDownYears / 2), 5, 1);
                }
            }
         else
            {function scwInputFormat(scwEleValue)
                {var scwArrSeed = new Array(),
                     scwArrInput = scwEle.value.
                                    split(new RegExp('[\\'+scwArrDelimiters.
                                                        join('\\')+']+','g'));

                 if (scwArrInput[0].length==0) scwArrInput.splice(0,1);

                 if (scwArrInput[scwArrInput.length-1].length==0)
                    scwArrInput.splice(scwArrInput.length-1,1);

                 scwBlnFullInputDate = false;

                 switch (scwArrInput.length)
                    {case 1:
                        {// Year only entry
                         scwArrSeed[0] = parseInt(scwArrInput[0],10);   // Year
                         scwArrSeed[1] = '6';                           // Month
                         scwArrSeed[2] = 1;                             // Day
                         break;
                        }
                     case 2:
                        {// Year and Month entry
                         scwArrSeed[0] =
                             parseInt(scwArrInput[scwDateInputSequence.
                                                    replace(/D/i,'').
                                                    search(/Y/i)],10);  // Year
                         scwArrSeed[1] = scwArrInput[scwDateInputSequence.
                                                    replace(/D/i,'').
                                                    search(/M/i)];      // Month
                         scwArrSeed[2] = 1;                             // Day
                         break;
                        }
                     case 3:
                        {// Day Month and Year entry
                         scwArrSeed[0] =
                             parseInt(scwArrInput[scwDateInputSequence.
                                                    search(/Y/i)],10);  // Year
                         scwArrSeed[1] = scwArrInput[scwDateInputSequence.
                                                    search(/M/i)];      // Month
                         scwArrSeed[2] =
                             parseInt(scwArrInput[scwDateInputSequence.
                                                    search(/D/i)],10);  // Day

                         scwBlnFullInputDate = true;
                         break;
                        }
                     default:
                        {// A stuff-up has led to more than three elements in
                         // the date.
                         scwArrSeed[0] = 0;     // Year
                         scwArrSeed[1] = 0;     // Month
                         scwArrSeed[2] = 0;     // Day
                        }
                    }

                 var scwExpValDay    = /^(0?[1-9]|[1-2]\d|3[0-1])$/,
                     scwExpValMonth  = new RegExp("^(0?[1-9]|1[0-2]|"        +
                                                  scwArrMonthNames.join("|") +
                                                  ")$","i"),
                     scwExpValYear   = /^(\d{1,2}|\d{4})$/;

                 // Apply validation and report failures
                 if (scwExpValYear.exec(scwArrSeed[0])  == null ||
                     scwExpValMonth.exec(scwArrSeed[1]) == null ||
                     scwExpValDay.exec(scwArrSeed[2])   == null)
                     {alert(scwInvalidDateMsg  +
                            scwInvalidAlert[0] + scwEleValue +
                            scwInvalidAlert[1]);
                      scwBlnFullInputDate = false;
                      scwArrSeed[0] = scwBaseYear +
                                      Math.floor(scwDropDownYears/2); // Year
                      scwArrSeed[1] = '6';                            // Month
                      scwArrSeed[2] = 1;                              // Day
                     }

                 return scwArrSeed;
                }

             scwArrSeedDate = scwInputFormat(scwEle.value);

             if (scwArrSeedDate[0]<100)
                scwArrSeedDate[0] += (scwArrSeedDate[0]>50)?1900:2000;

             if (scwArrSeedDate[1].search(/\d+/)!=0)
                {month = scwArrMonthNames.join('|').toUpperCase().
                            search(scwArrSeedDate[1].substr(0,3).
                                                    toUpperCase());
                 scwArrSeedDate[1] = Math.floor(month/4)+1;
                }

             scwSeedDate = new Date(scwArrSeedDate[0],
                                    scwArrSeedDate[1]-1,
                                    scwArrSeedDate[2]);
            }

         if (isNaN(scwSeedDate))
            {alert( scwInvalidDateMsg +
                    scwInvalidAlert[0] + scwEle.value +
                    scwInvalidAlert[1]);
             scwSeedDate = new Date(scwBaseYear +
                    Math.floor(scwDropDownYears/2),5,1);
             scwBlnFullInputDate=false;
            }
         else
            {// Test that the date is within range,
             // if not then set date to a sensible date in range.

             if ((new Date(scwBaseYear,0,1)) > scwSeedDate)
                {if (scwBlnStrict) alert(scwOutOfRangeMsg);
                 scwSeedDate = new Date(scwBaseYear,0,1);
                 scwBlnFullInputDate=false;
                }
             else
                {if ((new Date(scwBaseYear+scwDropDownYears-1,11,31))<
                      scwSeedDate)
                    {if (scwBlnStrict) alert(scwOutOfRangeMsg);
                     scwSeedDate = new Date(scwBaseYear +
                                            Math.floor(scwDropDownYears)-1,
                                                       11,1);
                     scwBlnFullInputDate=false;
                    }
                 else
                    {if (scwBlnStrict && scwBlnFullInputDate &&
                          (scwSeedDate.getDate()      != scwArrSeedDate[2] ||
                           (scwSeedDate.getMonth()+1) != scwArrSeedDate[1] ||
                           scwSeedDate.getFullYear()  != scwArrSeedDate[0]
                          )
                        )
                        {alert(scwDoesNotExistMsg);
                         scwSeedDate = new Date(scwSeedDate.getFullYear(),
                                                scwSeedDate.getMonth()-1,1);
                         scwBlnFullInputDate=false;
                        }
                    }
                }
            }

         for (var i=0;i<scwDisabledDates.length;i++)
            {if (!((typeof scwDisabledDates[i]      == 'object') &&
                   (scwDisabledDates[i].constructor == Date)))
                {if ((typeof scwDisabledDates[i]      == 'object') &&
                     (scwDisabledDates[i].constructor == Array))
                    {var scwPass = true;

                     if (scwDisabledDates[i].length !=2)
                        {alert(scwRangeDisablingError[0] +
                               scwDisabledDates[i] +
                               scwRangeDisablingError[1]);
                         scwPass = false;
                        }
                     else
                        {for (var j=0;j<scwDisabledDates[i].length;j++)
                            {if (!((typeof scwDisabledDates[i][j]
                                    == 'object') &&
                                   (scwDisabledDates[i][j].constructor
                                    == Date)))
                                {alert(scwDateDisablingError[0] +
                                       scwDisabledDates[i][j] +
                                       scwDateDisablingError[1]);
                                 scwPass = false;
                                }
                            }
                        }

                     if (scwPass &&
                         (scwDisabledDates[i][0] > scwDisabledDates[i][1])
                        )
                        {scwDisabledDates[i].reverse();}
                    }
                 else
                    {alert(scwDateDisablingError[0] + scwDisabledDates[i] +
                           scwDateDisablingError[1]);}
                }
            }

         scwMonthSum =  12*(scwSeedDate.getFullYear()-scwBaseYear)+
                            scwSeedDate.getMonth();

         // Set the drop down boxes.

         document.getElementById('scwYears').options.selectedIndex =
            Math.floor(scwMonthSum/12);
         document.getElementById('scwMonths').options.selectedIndex=
            (scwMonthSum%12);

         // Position the calendar box

         var offsetTop =parseInt(scwEle.offsetTop ,10) +
                        parseInt(scwEle.offsetHeight,10),
             offsetLeft=parseInt(scwEle.offsetLeft,10);

         scwTargetEle=scwEle;

         do {scwEle=scwEle.offsetParent;
             offsetTop +=parseInt(scwEle.offsetTop,10);
             offsetLeft+=parseInt(scwEle.offsetLeft,10);
            }
         while (scwEle.tagName!='BODY' && scwEle.tagName!='HTML');

         document.getElementById('scw').style.top =offsetTop +'px';
         document.getElementById('scw').style.left=offsetLeft+'px';

         if (document.getElementById('scwIframe'))
            {document.getElementById('scwIframe').style.top=offsetTop +'px';
             document.getElementById('scwIframe').style.left=offsetLeft+'px';
             document.getElementById('scwIframe').style.width=
                (document.getElementById('scw').offsetWidth-2)+'px';
             document.getElementById('scwIframe').style.height=
                (document.getElementById('scw').offsetHeight-2)+'px';
             document.getElementById('scwIframe').style.visibility='visible';
            }

         // Check whether or not dragging is allowed and display drag handle
         // if necessary

         document.getElementById('scwDrag').style.display=
             (scwAllowDrag)
                ?((document.getElementById('scwIFrame')||
                   document.getElementById('scwIEgte7'))?'block':'table-row')
                :'none';

         // Display the month

         scwShowMonth(0);

         // Show it on the page

         document.getElementById('scw').style.visibility='visible';

         if (typeof event=='undefined')
                {scwSourceEle.parentNode.
                        addEventListener("click",scwStopPropagation,false);
                }
         else   {event.cancelBubble = true;}
        }

    function scwHide()
        {document.getElementById('scw').style.visibility='hidden';
         if (document.getElementById('scwIframe'))
            {document.getElementById('scwIframe').style.visibility='hidden';}

         if (typeof scwNextAction!='undefined' && scwNextAction!=null)
             {scwNextActionReturn = scwNextAction();
              // Explicit null set to prevent closure causing memory leak
              scwNextAction = null;
             }
        }

    function scwCancel(scwEvt)
        {if (scwClickToHide) scwHide();
         scwStopPropagation(scwEvt);
        }

    function scwStopPropagation(scwEvt)
        {if (scwEvt.stopPropagation)
                scwEvt.stopPropagation();    // Capture phase
         else   scwEvt.cancelBubble = true;  // Bubbling phase
        }

    function scwBeginDrag(event)
        {var elementToDrag = document.getElementById('scw');

         var deltaX    = event.clientX,
             deltaY    = event.clientY,
             offsetEle = elementToDrag;

         do {deltaX   -= parseInt(offsetEle.offsetLeft,10);
             deltaY   -= parseInt(offsetEle.offsetTop ,10);
             offsetEle = offsetEle.offsetParent;
            }
         while (offsetEle.tagName!='BODY' &&
                offsetEle.tagName!='HTML');

         if (document.addEventListener)
                {document.addEventListener('mousemove',
                                           moveHandler,
                                           true);        // Capture phase
                 document.addEventListener('mouseup',
                                           upHandler,
                                           true);        // Capture phase
                }
         else   {elementToDrag.attachEvent('onmousemove',
                                           moveHandler); // Bubbling phase
                 elementToDrag.attachEvent('onmouseup',
                                             upHandler); // Bubbling phase
                 elementToDrag.setCapture();
                }

         scwStopPropagation(event);

         function moveHandler(e)
            {if (!e) e = window.event;

             elementToDrag.style.left = (e.clientX - deltaX) + 'px';
             elementToDrag.style.top  = (e.clientY - deltaY) + 'px';

             if (document.getElementById('scwIframe'))
                {document.getElementById('scwIframe').style.left =
                    (e.clientX - deltaX) + 'px';
                 document.getElementById('scwIframe').style.top  =
                    (e.clientY - deltaY) + 'px';
                }

             scwStopPropagation(e);
            }

         function upHandler(e)
            {if (!e) e = window.event;

             if (document.removeEventListener)
                    {document.removeEventListener('mousemove',
                                                  moveHandler,
                                                  true);     // Capture phase
                     document.removeEventListener('mouseup',
                                                  upHandler,
                                                  true);     // Capture phase
                    }
             else   {elementToDrag.detachEvent('onmouseup',
                                                 upHandler); // Bubbling phase
                     elementToDrag.detachEvent('onmousemove',
                                               moveHandler); // Bubbling phase
                     elementToDrag.releaseCapture();
                    }

             scwStopPropagation(e);
            }
        }

    function scwShowMonth(scwBias)
        {

         var scwShowDate  = new Date(Date.parse(new Date().toDateString())),
             scwStartDate = new Date(),
             scwSaveBackground,
             scwSaveText;

         scwSelYears  = document.getElementById('scwYears');
         scwSelMonths = document.getElementById('scwMonths');

         if (scwSelYears.options.selectedIndex>-1)
            {scwMonthSum=12*(scwSelYears.options.selectedIndex)+scwBias;
             if (scwSelMonths.options.selectedIndex>-1)
                {scwMonthSum+=scwSelMonths.options.selectedIndex;}
            }
         else
            {if (scwSelMonths.options.selectedIndex>-1)
                {scwMonthSum+=scwSelMonths.options.selectedIndex;}
            }

         scwShowDate.setFullYear(scwBaseYear + Math.floor(scwMonthSum/12),
                                 (scwMonthSum%12),
                                 1);

         // If the Week numbers are displayed, shift the week day names
         // to the right.
         document.getElementById("scwWeek_").style.display=
             (scwWeekNumberDisplay)?'block':'none';

         if ((12*parseInt((scwShowDate.getFullYear()-scwBaseYear),10)) +
             parseInt(scwShowDate.getMonth(),10) < (12*scwDropDownYears)  &&
             (12*parseInt((scwShowDate.getFullYear()-scwBaseYear),10)) +
             parseInt(scwShowDate.getMonth(),10) > -1)
            {scwSelYears.options.selectedIndex=Math.floor(scwMonthSum/12);
             scwSelMonths.options.selectedIndex=(scwMonthSum%12);

             scwCurMonth = scwShowDate.getMonth();

             scwShowDate.setDate((((scwShowDate.
                                    getDay()-scwWeekStart)<0)?-6:1)+
                                 scwWeekStart-scwShowDate.getDay());

             scwStartDate = new Date(scwShowDate);

             var scwFoot = document.getElementById('scwFoot');

             function scwFootOutput()   {scwSetOutput(scwDateNow);}

             function scwFootOver()
                {document.getElementById('scwFoot').style.color=
                    scwTodayHighlight;
                 document.getElementById('scwFoot').style.fontWeight='bold';
                }

             function scwFootOut()
                {document.getElementById('scwFoot').style.color=scwTodayText;
                 document.getElementById('scwFoot').style.fontWeight='normal';
                }

             if (scwDisabledDates.length==0)
                {if (scwActiveToday && scwParmActiveToday)
                    {scwFoot.onclick     =scwFootOutput;
                     scwFoot.onmouseover =scwFootOver;
                     scwFoot.onmouseout  =scwFootOut;
                     scwFoot.style.cursor=
                         (document.getElementById('scwIFrame')||
                          document.getElementById('scwIEgte7'))
                             ?'hand':'pointer';
                    }
                 else
                    {scwFoot.onclick     =null;
                     if (document.addEventListener)
                            {scwFoot.addEventListener('click',
                                                      scwStopPropagation,
                                                      false);}
                     else   {scwFoot.attachEvent('onclick',
                                                 scwStopPropagation);}
                     scwFoot.onmouseover =null;
                     scwFoot.onmouseout  =null;
                     scwFoot.style.cursor='default';
                    }
                }
             else
                {for (var k=0;k<scwDisabledDates.length;k++)
                    {if (!scwActiveToday || !scwParmActiveToday ||
                         ((typeof scwDisabledDates[k] == 'object')            &&
                             (((scwDisabledDates[k].constructor == Date)      &&
                               scwDateNow.valueOf() == scwDisabledDates[k].
                                                            valueOf()
                              ) ||
                              ((scwDisabledDates[k].constructor == Array)     &&
                               scwDateNow.valueOf() >= scwDisabledDates[k][0].
                                                        valueOf()             &&
                               scwDateNow.valueOf() <= scwDisabledDates[k][1].
                                                        valueOf()
                              )
                             )
                         )
                        )
                        {scwFoot.onclick     =null;
                         if (document.addEventListener)
                                {scwFoot.addEventListener('click',
                                                          scwStopPropagation,
                                                          false);
                                }
                         else   {scwFoot.attachEvent('onclick',
                                                     scwStopPropagation);
                                }
                         scwFoot.onmouseover =null;
                         scwFoot.onmouseout  =null;
                         scwFoot.style.cursor='default';
                         break;
                        }
                     else
                        {scwFoot.onclick     =scwFootOutput;
                         scwFoot.onmouseover =scwFootOver;
                         scwFoot.onmouseout  =scwFootOut;
                         scwFoot.style.cursor=
                             (document.getElementById('scwIFrame')||
                              document.getElementById('scwIEgte7'))
                                  ?'hand':'pointer';
                        }
                    }
                }

             function scwSetOutput(scwOutputDate)
                {scwTargetEle.value =
                    scwOutputDate.scwFormat(scwDateOutputFormat);
                 scwHide();
                }

             function scwCellOutput(scwEvt)
                {var scwEle = scwEventTrigger(scwEvt),
                     scwOutputDate = new Date(scwStartDate);

                 if (scwEle.nodeType==3) scwEle=scwEle.parentNode;

                 scwOutputDate.setDate(scwStartDate.getDate() +
                                         parseInt(scwEle.id.substr(8),10));

                 scwSetOutput(scwOutputDate);
                }

             function scwHighlight(e)
                {var scwEle = scwEventTrigger(e);

                 if (scwEle.nodeType==3) scwEle=scwEle.parentNode;

                 scwSaveText        =scwEle.style.color;
                 scwSaveBackground  =scwEle.style.backgroundColor;

                 scwEle.style.color             =scwHighlightText;
                 scwEle.style.backgroundColor   =scwHighlightBackground;

                 return true;
                }

             function scwUnhighlight(e)
                {var scwEle = scwEventTrigger(e);

                 if (scwEle.nodeType==3) scwEle =scwEle.parentNode;

                 scwEle.style.backgroundColor   =scwSaveBackground;
                 scwEle.style.color             =scwSaveText;

                 return true;
                }

             function scwEventTrigger(e)
                {if (!e) e = event;
                 return e.target||e.srcElement;
                }

            function scwWeekNumber(scwInDate)
                {// The base day in the week of the input date
                 var scwInDateWeekBase = new Date(scwInDate);

                 scwInDateWeekBase.setDate(scwInDateWeekBase.getDate()
                                            - scwInDateWeekBase.getDay()
                                            + scwWeekNumberBaseDay
                                            + ((scwInDate.getDay()>
                                                scwWeekNumberBaseDay)?7:0));

                 // The first Base Day in the year
                 var scwFirstBaseDay = new Date(scwInDateWeekBase.getFullYear(),0,1)

                 scwFirstBaseDay.setDate(scwFirstBaseDay.getDate()
                                            - scwFirstBaseDay.getDay()
                                            + scwWeekNumberBaseDay
                                        );

                 if (scwFirstBaseDay < new Date(scwInDateWeekBase.getFullYear(),0,1))
                    {scwFirstBaseDay.setDate(scwFirstBaseDay.getDate()+7);}

                 // Start of Week 01
                 var scwStartWeekOne = new Date(scwFirstBaseDay
                                                - scwWeekNumberBaseDay
                                                + scwInDate.getDay());

                 if (scwStartWeekOne > scwFirstBaseDay)
                    {scwStartWeekOne.setDate(scwStartWeekOne.getDate()-7);}

                 var scwWeekNo = "0" + (Math.round((scwInDateWeekBase -
                                                    scwFirstBaseDay)/604800000,0) + 1);

                 // Return the last two characters in the week number string

                 return scwWeekNo.substring(scwWeekNo.length-2,scwWeekNo.length);
                }

             var scwCells = document.getElementById('scwCells');

             for (i=0;i<scwCells.childNodes.length;i++)
                {var scwRows = scwCells.childNodes[i];
                 if (scwRows.nodeType==1 && scwRows.tagName=='TR')
                    {if (scwWeekNumberDisplay)
                        {//Calculate the week number using scwShowDate
                         scwRows.childNodes[0].innerHTML=scwWeekNumber(scwShowDate);
                         scwRows.childNodes[0].style.display='block';
                        }
                     else
                        {scwRows.childNodes[0].style.display='none';}

                     for (j=1;j<scwRows.childNodes.length;j++)
                        {var scwCols = scwRows.childNodes[j];
                         if (scwCols.nodeType==1 && scwCols.tagName=='TD')
                            {scwRows.childNodes[j].innerHTML=
                                scwShowDate.getDate();
                             var scwCellStyle=scwRows.childNodes[j].style,
                                 scwDisabled =
                                    (scwOutOfRangeDisable &&
                                     (scwShowDate < (new Date(scwBaseYear,0,1))
                                      ||
                                      scwShowDate > (new Date(scwBaseYear+
                                                              scwDropDownYears-
                                                              1,11,31))
                                     )
                                    )?true:false;

                             for (var k=0;k<scwDisabledDates.length;k++)
                                {if ((typeof scwDisabledDates[k]=='object')
                                     &&
                                     (scwDisabledDates[k].constructor ==
                                      Date
                                     )
                                     &&
                                     scwShowDate.valueOf() ==
                                        scwDisabledDates[k].valueOf()
                                    )
                                    {scwDisabled = true;}
                                 else
                                    {if ((typeof scwDisabledDates[k]=='object')
                                         &&
                                         (scwDisabledDates[k].constructor ==
                                          Array
                                         )
                                         &&
                                         scwShowDate.valueOf() >=
                                             scwDisabledDates[k][0].valueOf()
                                         &&
                                         scwShowDate.valueOf() <=
                                             scwDisabledDates[k][1].valueOf()
                                        )
                                        {scwDisabled = true;}
                                    }
                                }

                             if (scwDisabled ||
                                 !scwEnabledDay[j-1+(7*((i*scwCells.
                                                          childNodes.
                                                          length)/6))] ||
                                 !scwPassEnabledDay[(j-1+(7*(i*scwCells.
                                                               childNodes.
                                                               length/6)))%7]
                                )
                                {scwRows.childNodes[j].onclick     =null;
                                 scwRows.childNodes[j].onmouseover =null;
                                 scwRows.childNodes[j].onmouseout  =null;
                                 scwRows.childNodes[j].style.cursor='default';

                                 if (scwShowDate.getMonth()!=scwCurMonth)
                                    {scwCellStyle.color=scwDisabledExMonthText;
                                     scwCellStyle.backgroundColor=
                                         scwDisabledExMonthBackground;
                                    }
                                 else if (scwBlnFullInputDate &&
                                          scwShowDate.toDateString()==
                                          scwSeedDate.toDateString())
                                    {scwCellStyle.color=scwDisabledInDateText;
                                     scwCellStyle.backgroundColor=
                                         scwDisabledInDateBackground;
                                    }
                                 else if (scwShowDate.getDay()%6==0)
                                    {scwCellStyle.color=scwDisabledWeekendText;
                                     scwCellStyle.backgroundColor=
                                         scwDisabledWeekendBackground;
                                    }
                                 else
                                    {scwCellStyle.color=scwDisabledCellText;
                                     scwCellStyle.backgroundColor=
                                         scwDisabledCellBackground;
                                    }
                                }
                             else
                                {scwRows.childNodes[j].onclick      =
                                    scwCellOutput;
                                 scwRows.childNodes[j].onmouseover  =
                                    scwHighlight;
                                 scwRows.childNodes[j].onmouseout   =
                                    scwUnhighlight;
                                 scwRows.childNodes[j].style.cursor =
                                    (document.getElementById('scwIFrame')||
                                     document.getElementById('scwIEgte7'))
                                        ?'hand':'pointer';

                                 if (scwShowDate.getMonth()!=scwCurMonth) 
                                 {scwCellStyle.color=scwExMonthText; 
                                 scwCellStyle.backgroundColor= 
                                 scwExMonthBackground; } else if 
                                 (scwBlnFullInputDate && 
                                 scwShowDate.toDateString()== 
                                 scwSeedDate.toDateString()) 
                                 {scwCellStyle.color=scwInDateText; 
                                 scwCellStyle.backgroundColor= 
                                 scwInDateBackground; } else if 
                                 (scwShowDate.getDay()%6==0) 
                                 {scwCellStyle.color=scwWeekendText; 
                                 scwCellStyle.backgroundColor= 
                                 scwWeekendBackground; } else 
                                 {scwCellStyle.color=scwCellText; 
                                 scwCellStyle.backgroundColor= 
                                 scwCellBackground; } }

                             scwShowDate.setDate(scwShowDate.getDate()+1);
                            }
                        }
                    }
                }
            }
        }

     function formatPadLeft(padStr, padToLength) { var result = ''; for (var 
     i=0;i<(padToLength - padStr.length);i++) { result += '0';} return(result 
     + padStr); }
     
     function formatDate(dateObj){
     		     var scwFormat = 'DD/MM/YYYY';
             var charCount = 0,
             codeChar  = '',
             result    = '';

             for (var i=0;i<=scwFormat.length;i++)
                {if (i<scwFormat.length && scwFormat.charAt(i)==codeChar)
                        {
                         charCount++;
                        }
                 else   {switch (codeChar)
                            {case 'y': case 'Y':
                                result += formatPadLeft(
                                                        (dateObj.getFullYear()%Math.pow(10,charCount)).toString(),
                                                        charCount);
                                break;
                             case 'm': case 'M':
                                result += formatPadLeft(
                                                        (dateObj.getMonth()+1).toString(),
                                                        charCount);
                                break;
                             case 'd': case 'D':
                                // If we find a D, get the date and format it
                                result += formatPadLeft(
                                                        dateObj.getDate().toString(),
                                                        charCount);
                                break;
                             default:
                                // Copy any unrecognised characters across
                                while (charCount-- > 0) {result += codeChar;}
                            }

                         if (i<scwFormat.length)
                            {// Store the character we have just worked on
                             codeChar  = scwFormat.charAt(i);
                             charCount = 1;
                            }
                        }
                }
             return result;
    }
            
    function setToday(fromEle, toEle){
				var nowDate=new Date();
				var dateStr=formatDate(nowDate);
				fromEle.value = dateStr;
				toEle.value = dateStr;
				
		}

    function setYesterday(fromEle, toEle){
				var nowDate=new Date();
				var dateStr = formatDate(new Date(nowDate - 86400000));
				fromEle.value = dateStr;
				toEle.value = dateStr;
		}
    //add by linrui 20190911 for 3 days  7days  1 mounth
    function set3day(fromEle, toEle){
    	var nowDate=new Date();
    	var dateStr = formatDate(new Date(nowDate - 259200000));
    	fromEle.value = dateStr;
    	toEle.value = formatDate(nowDate);
    }
    
    function set7day(fromEle, toEle){
    	var nowDate=new Date();
    	var dateStr = formatDate(new Date(nowDate - 604800000));
    	fromEle.value = dateStr;
    	toEle.value = formatDate(nowDate);
    }
    
    function setMonth(fromEle, toEle){
    	var nowDate=new Date();
    	var dt = new Date();
        dt.setMonth( dt.getMonth()-1 );
    	var dateStr = formatDate(dt);
    	fromEle.value = dateStr;
    	toEle.value = formatDate(nowDate);
    }
    
    function set3Month(fromEle, toEle){
    	var nowDate=new Date();
    	var dt = new Date();
        dt.setMonth( dt.getMonth()-3 );
    	var dateStr = formatDate(dt);
    	fromEle.value = dateStr;
    	toEle.value = formatDate(nowDate);
    }
    
    function set6Month(fromEle, toEle){
    	var nowDate=new Date();
    	var dt = new Date();
        dt.setMonth( dt.getMonth()-6 );
    	var dateStr = formatDate(dt);
    	fromEle.value = dateStr;
    	toEle.value = formatDate(nowDate);
    }
    
    function setYear(fromEle, toEle){
    	var nowDate=new Date();
    	var dt = new Date();
        dt.setYear( dt.getFullYear()-1 );
    	var dateStr = formatDate(dt);
    	fromEle.value = dateStr;
    	toEle.value = formatDate(nowDate);
    }
    
    function set2Year(fromEle, toEle){
    	var nowDate=new Date();
    	var dt = new Date();
        dt.setYear( dt.getFullYear()-2 );
    	var dateStr = formatDate(dt);
    	fromEle.value = dateStr;
    	toEle.value = formatDate(nowDate);
    }
    
    function set3Year(fromEle, toEle){
    	var nowDate=new Date();
    	var dt = new Date();
        dt.setYear( dt.getFullYear()-3 );
    	var dateStr = formatDate(dt);
    	fromEle.value = dateStr;
    	toEle.value = formatDate(nowDate);
    }
    
    //add by linrui end
    function setDateRange(selEle, fromEle, toEle){
    	var sel = selEle.value;
    	if(sel == '1'){//today
    		setToday(fromEle, toEle);
    	}
    	if(sel == '2'){//yesterday
    		setYesterday(fromEle, toEle);
    	}
    	if(sel == '3'){//past 3 day
    		set3day(fromEle, toEle);
    	}
    	if(sel == '4'){//past 7 day
    		set7day(fromEle, toEle);
    	}
    	if(sel == '5'){//past month
    		setMonth(fromEle, toEle);
    	}
    	if(sel == '6'){//past 3 month
    		set3Month(fromEle, toEle);
    	}
    	if(sel == '7'){//past 6 month
    		set6Month(fromEle, toEle);
    	}
    	if(sel == '8'){//past 1 year
    		setYear(fromEle, toEle);
    	}
    	if(sel == '9'){//past 2 year
    		set2Year(fromEle, toEle);
    	}
    	if(sel == '10'){//past 3 year
    		set3Year(fromEle, toEle);
    	}
    }
    
    document.write(
     "<!--[if gte IE 7]>" +
        "<div id='scwIEgte7'></div>" +
     "<![endif]-->" +
     "<!--[if lt  IE 7]>" +
        "<iframe class='scw' src='/cib/common/blank.jsp' " +
                "id='scwIframe' name='scwIframe' " +
                "frameborder='0'>" +
        "</iframe>" +
     "<![endif]-->" +
     "<table id='scw' class='scw' onclick='scwCancel(event);'>" +
       "<tr class='scw'>" +
         "<td class='scw'>" +
           "<table class='scwHead' id='scwHead' width='100%' " +
                    "onClick='scwStopPropagation(event);' " +
                    "cellspacing='0' cellpadding='0'>" +
            "<tr id='scwDrag' style='display:none;'>" +
                "<td colspan='4' class='scwDrag' " +
                    "onmousedown='scwBeginDrag(event);'>" +
                    "<div id='scwDragText'></div>" +
                "</td>" +
            "</tr>" +
            "<tr class='scwHead'>" +
                 "<td class='scwHead'>" +
                    "<input class='scwHead' type='button' value='<' " +
                            "onclick='scwShowMonth(-1);'  /></td>" +
                 "<td class='scwHead'>" +
                    "<select id='scwMonths' class='scwHead' " +
                            "onChange='scwShowMonth(0);'>" +
                    "</select>" +
                 "</td>" +
                 "<td class='scwHead'>" +
                    "<select id='scwYears' class='scwHead' " +
                            "onChange='scwShowMonth(0);'>" +
                    "</select>" +
                 "</td>" +
                 "<td class='scwHead'>" +
                    "<input class='scwHead' type='button' value='>' " +
                            "onclick='scwShowMonth(1);' /></td>" +
                "</tr>" +
              "</table>" +
            "</td>" +
          "</tr>" +
          "<tr class='scw'>" +
            "<td class='scw'>" +
              "<table class='scwCells' align='center'>" +
                "<thead>" +
                  "<tr><td class='scwWeekNumberHead' id='scwWeek_' ></td>");

    for (i=0;i<7;i++)
        document.write( "<td class='scwWeek' id='scwWeekInit" + i + "'></td>");

    document.write("</tr>" +
                "</thead>" +
                "<tbody id='scwCells' " +
                        "onClick='scwStopPropagation(event);'>");

    for (i=0;i<6;i++)
        {document.write(
                    "<tr>" +
                      "<td class='scwWeekNo' id='scwWeek_" + i + "'></td>");
         for (j=0;j<7;j++)
            {document.write(
                        "<td class='scwCells' id='scwCell_" + (j+(i*7)) +
                        "'></td>");
            }

         document.write(
                    "</tr>");
        }

    document.write(
                "</tbody>");

    if ((new Date(scwBaseYear + scwDropDownYears, 11, 32)) > scwDateNow &&
        (new Date(scwBaseYear, 0, 0))                      < scwDateNow)
        {document.write(
                  "<tfoot class='scwFoot'>" +
                    "<tr class='scwFoot'>" +
                      "<td class='scwFoot' id='scwFoot' colspan='8'>" +
                      "</td>" +
                    "</tr>" +
                  "</tfoot>");
        }

    document.write(
              "</table>" +
            "</td>" +
          "</tr>" +
        "</table>");

    if (document.addEventListener)
            {document.addEventListener('click',scwHide, false);}
    else    {document.attachEvent('onclick',scwHide);}

