
(function () {
    "use strict";

    const tableBodySelector = ".table-body";
    const searchBoxSelector = ".search-inp";

    const tableServletAPI = $(tableBodySelector).attr("data-api");

    var colsLength = $('th') ? $('th').length : 1;

    var results;
    if (tableServletAPI) {
        queryFetch().then(data => {
            results = data;
            populateRows(results);
        })
    }

    async function queryFetch() {
        const res = await fetch(tableServletAPI + ".json", {
            method: 'GET',
            headers: { "Content-Type": "application/json" }
        });
        return await res.json();
    }

    function populateRows(results) {
        $(tableBodySelector).html("");
        if (results && results.length > 0) {
            results.forEach(row => {
                var rowHTML = `<tr>`
                var col = "";
                row.values.forEach(colVal => {
                    col += `<td>` + colVal + `</td>`
                })

                rowHTML += col + `</td>`
                $(tableBodySelector).append(rowHTML);
            })
        } else {
            $(tableBodySelector).append(
                "<tr><td colspan='"+ colsLength +"'>No Results Found</td></tr>"
            );
        }
    }

    $(searchBoxSelector).keyup(function (event) {
        if (event.keyCode === 13) {
            var filteredResults = [];
            var searchTerm = $(searchBoxSelector).val();
            if (searchTerm && results && results.length > 0) {
                results.forEach(row => {
                    if (row.searchKey && row.searchKey.includes(searchTerm)) {
                        filteredResults.push(row);
                    }
                })

                populateRows(filteredResults);
            }
        }
    });



}());