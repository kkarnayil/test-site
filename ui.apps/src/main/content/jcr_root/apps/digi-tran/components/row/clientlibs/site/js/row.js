(function ($, $document) {
    "use strict";
		var selectors = {
        	dialogContent: ".cmp-row__editor"
    	 };

   		 $document.on("dialog-loaded", function(e) {
 		var $dialog = e.dialog;
        if ($dialog.length) {
            var $dialogContent = $dialog.find(selectors.dialogContent);
            alert("s"+ $dialog.find(selectors.dialogContent));
             if ($dialogContent) {
              	alert('Its coming here on dialog open');
             }
        }
         });

    })($, $(document));