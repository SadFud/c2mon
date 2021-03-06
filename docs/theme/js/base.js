/* Search */

function getSearchTerm()
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++)
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == 'q') {
            return sParameterName[1];
        }
    }
}

$(document).ready(function() {
    var search_term = getSearchTerm(),
        $search_modal = $('#mkdocs_search_modal');

    if(search_term) {
        $search_modal.modal();
    }

    $search_modal.on('shown.bs.modal', function () {
        $search_modal.find('#mkdocs-search-query').focus();
    });
});


/* Highlight */
$( document ).ready(function() {
    hljs.initHighlightingOnLoad();
    $('table').addClass('table table-striped table-hover');
});


$('body').scrollspy({
    target: '.bs-sidebar',
});

/* Toggle the `clicky` class on the body when clicking links to let us
   retrigger CSS animations. See ../css/base.css for more details. */
$('a').click(function(e) {
    $('body').toggleClass('clicky');
});

/* Prevent disabled links from causing a page reload */
$("li.disabled a").click(function() {
    event.preventDefault();
});


/* Version switcher */
$.getJSON( base_url + "/../versions.json", function(versionDescriptor) {
    var items = [];

    var selected = window.location.pathname.split('/')[3];

    if (selected === 'latest') {
        selected = versionDescriptor.release;
    } else if (selected === 'snapshot') {
        selected = versionDescriptor.snapshot;
    }

    versionDescriptor.versions.forEach(function (version) {
        var extra = '';

        if (version === versionDescriptor.release) {
            extra = ' [latest]';
        } else if (version === versionDescriptor.snapshot) {
            extra = ' [snapshot]';
        }

        if (version === selected) {
            items.push( "<option selected>" + version + extra + "</option>" );
        } else {
            items.push( "<option value='" + base_url + "/../" + version + "'>" + version + extra + "</option>" );
        }
    });

    $("<select />", {
        onchange: 'window.location = this.value',
        id: 'version-switcher-control',
        "class": "form-control",
        html: items.join("")
    }).appendTo("#version-switcher");

}).error(function(error) {
    console.log('error loading versions.json');
});

