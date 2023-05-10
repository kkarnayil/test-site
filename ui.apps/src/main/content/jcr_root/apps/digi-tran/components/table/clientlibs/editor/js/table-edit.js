(function ($, Coral) {
  "use strict";
  var selectors = {
    dialogContent: ".cmp-table__editor"
  };

  $(document).on("dialog-loaded", function (e) {

    var $dialog = e.dialog;
    var $dialogContent = $dialog.find(selectors.dialogContent);
    var dialogContent = $dialogContent.length > 0 ? $dialogContent[0] : undefined;

    if (dialogContent) {
      var coralTab = $(".table-tabs coral-tablist coral-tab");
      var filterTab = coralTab[1];

      var element = $(".filter-checkbox");
      Coral.commons.ready(element, function (component) {

        var isFilterChecked = component[0]._checked;
        hideShowTab(isFilterChecked, filterTab);

        $(component).on("change", function (event) {
          hideShowTab(event.target.checked, filterTab);
        })
      });
    }

  });

  function hideShowTab(checked, tab) {
    if (checked) {
      tab.show();
    } else {
      tab.hide();
    }
  }

})(jQuery, Coral);