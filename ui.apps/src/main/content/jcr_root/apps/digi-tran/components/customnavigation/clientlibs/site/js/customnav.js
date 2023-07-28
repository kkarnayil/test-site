(function ($) {
  "use strict";
  var selectors = {
    dialogContent: ".custom-nav",
    multifieldSection: ".childpages-multifield",
    multifieldEl: "coral-multifield",
    getChildPagesBtn: "button[form='fetchChildPagesBtn']",
    titleFieldItem:"input[name='./linkHeading']",
    pathFieldItem: "input[name='./link']",
    sortFieldItem: "input[name='./sortOrder']",
    rootPathFieldEl: "foundation-autocomplete [name='./navigationRoot'] input"
  };

  var currentResource;

  $(document).on("dialog-loaded", function (e) {

    var $dialog = e.dialog;
    var $dialogContent = $dialog.find(selectors.dialogContent);
    var dialogContent = $dialogContent.length > 0 ? $dialogContent[0] : undefined;

    if (dialogContent) {

      var multifieldContainer = dialogContent.querySelector(selectors.multifieldSection);
      var multified = dialogContent.querySelector(selectors.multifieldEl);
      currentResource = $dialog.attr("action");
  
      var confirmDialog = getConfirmDialog(dialogContent, multified, multifieldContainer);

      if (multified?.items?.length > 0) {
        showMultifield(multifieldContainer);
      }

      $(selectors.getChildPagesBtn).click(function () {
        handleGetAllChildClick(multified, multifieldContainer, confirmDialog);
      });
    }

    $dialog.on("click", ".cq-dialog-submit", function (e) {
      removeDisabledFields(e, multified);
    });

  });

  function handleGetAllChildClick(multified, multifieldContainer, confirmDialog) {

    if (multified?.items?.length > 0) {
      confirmDialog.show();
    } else {
      populateMultifield(multified, multifieldContainer);
    }
  }

  function populateMultifield(multified, multifieldContainer) {
    var navRoot = $(selectors.rootPathFieldEl).val();

    if(!navRoot){
      return;
    }

    var wait = new Coral.Wait();
    $(wait).css('display','block').css('margin', '0 auto');
    
    multifieldContainer.insertBefore(wait, multifieldContainer.firstChild);
    // appending nav root in suffix
    var api = currentResource +".model.json"+navRoot;
    fetch(api)
      .then(response => {
        // Check if the response is successful (status code between 200 and 299)
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        // Parse the response body as JSON
        return response.json();
      })
      .then(data => {
        addChildPagetoMultifield(data, multified, multifieldContainer);
        debugger;
        wait.remove();
      })
      .catch(error => {
        // Handle any errors that occurred during the fetch
        console.error('Fetch error:', error);
        wait.remove();
      });
  }

  function addChildPagetoMultifield(data, multified, multifieldContainer){

    if(data?.parentChildPages?.length <= 0){
      return;
    }

    var i = 0;
    // call back method to check the selected visibility rules.
    const callback = function (mutationsList, observer) {
      for (const mutation of mutationsList) {
        var multifieldItem = $(mutation.addedNodes);
        if (multifieldItem && multifieldItem.length > 0) {
          $(multifieldItem).find(selectors.titleFieldItem).val(data.parentChildPages[i].title);
          $(multifieldItem).find(selectors.pathFieldItem).val(data.parentChildPages[i].path);
          $(multifieldItem).find(selectors.sortFieldItem).val(data.parentChildPages[i].sortOrder);
        }
        i++;
      }

      // disconnect observer when all rules are populated.
      if (i == 2) {
        observer.disconnect();
      }
    };

    const config = {
      childList: true
    };

    // Create an observer instance linked to the callback function
    const observer = new MutationObserver(callback);

    // Start observing the target node for configured mutations
    observer.observe(multified, config);

    for (var j = 0; j < data?.parentChildPages?.length; j++) {
      multified.items.add();
    }
    showMultifield(multifieldContainer);


  }


  function showMultifield(multifieldContainer) {
    $(multifieldContainer).removeClass("hide");
    $(multifieldContainer).find("button[coral-multifield-add]").remove();
    $(multifieldContainer).find("coral-icon[icon='moveUpDown']").hide();
  }

  function getConfirmDialog(dialogContent, multified, multifieldContainer) {
    var confirmDialog = dialogContent.querySelector("#confirmDialog");
    if (!confirmDialog) {
      confirmDialog = new Coral.Dialog().set({
        id: 'confirmDialog',
        header: {
          innerHTML: 'Confirm Replacing List Items'
        },
        content: {
          innerHTML: 'You will replace the list items, if you accept this message'
        },
        footer: {
          innerHTML: '<button id="cancelButton" is="coral-button" variant="default" coral-close>Cancel</button><button id="acceptButton" is="coral-button" variant="primary">Accept</button>'
        }
      });
      dialogContent.appendChild(confirmDialog);
    }

    confirmDialog.on('coral-overlay:close', function (event) {
      console.log("Replace action cancelled");
    });
    confirmDialog.on('click', '#acceptButton', function () {
      multified.items.clear();
      confirmDialog.hide();
      populateMultifield(multified, multifieldContainer);
    });

    return confirmDialog;
  }

  function removeDisabledFields(event, multified){
    if(multified){
      event.stopPropagation();
      event.preventDefault();
      var $form = $(multified).closest("form.foundation-form");    
      $(multified).find('[disabled]').removeAttr('disabled')
      $form.submit();
    }
  }

})(jQuery);