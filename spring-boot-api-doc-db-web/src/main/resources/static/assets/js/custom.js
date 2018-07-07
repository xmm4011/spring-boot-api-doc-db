//Loads the correct sidebar on window load,
//collapses the sidebar on window resize.
// Sets the min-height of #page-wrapper to window size
$(function() {
	$(window).bind("load resize", function() {
		var topOffset = 50;
		var width = (this.window.innerWidth > 0) ? this.window.innerWidth : this.screen.width;
		if(width < 768) {
			$('div.navbar-collapse').addClass('collapse');
			topOffset = 100; // 2-row-menu
		} else {
			$('div.navbar-collapse').removeClass('collapse');
		}

		var height = ((this.window.innerHeight > 0) ? this.window.innerHeight : this.screen.height) - 1;
		height = height - topOffset;
		if(height < 1) height = 1;
		if(height > topOffset) {
			$("#page-wrapper").css("min-height", (height) + "px");
		}
	});

	var url = window.location;
	// var element = $('ul.nav a').filter(function() {
	//     return this.href == url;
	// }).addClass('active').parent().parent().addClass('in').parent();
	var element = $('ul.nav a').filter(function() {
		return this.href == url;
	}).addClass('active').parent();

	while(true) {
		if(element.is('li')) {
			element = element.parent().addClass('in').parent();
		} else {
			break;
		}
	}
});

function getParameter(name){
	var value = String(location).match(new RegExp('[?&]' + name + '=([^&]*)(&?)', 'i'));
	return value ? value[1] : null;
}

$(function() {
    initProject();
});

var project = null;
function initProject() {
    var name = getParameter('name');
    $.ajax({
        type: 'GET',
        url: '/api-doc/project/' + name,
        success: function (data) {
            if (data.success) {
                project = data.data;
                initMenu();
            } else {
                alert(data.message);
            }
        },
        error: function (xhr, errorType, error) {
            alert(error);
        }
    });
}

function initMenu() {
	initMenuTabs();
}

function initMenuTabs() {
    var menus = project.menus;

    var tabIndex = 0;
    $.each(menus, function(index, item) {
        if(getParameter('tab') === item.desc) {
            tabIndex = index;
        }
        $("#tabs").append('<li><a href="javascript:void(0);" data-index=' + index + ' title="' + item.desc + '">' + item.desc + '</a></li>')
    });

    initMenuTabsEvents();
    initTabMenus(menus[tabIndex]);
}

function initMenuTabsEvents() {
    var menus = project.menus;
	$("#tabs li a").click(function() {
        $('#page-wrapper').addClass('hidden');
		initTabMenus(menus[$(this).attr('data-index')]);
	});
}

function initTabMenus(menu) {
	var html = '';
	$.each(menu.childs, function(pageIndex, pageItem) {
		html += '<li>';
		html += '	<a href="javascript:void(0);" data-type="menu" data-id="' + pageItem.id + '" data-version="' + pageItem.version + '">' + pageItem.desc + '<span class="fa arrow"></span></a>';
		html += '	<ul class="nav nav-second-level">';
		$.each(pageItem.childs, function(menuIndex, menuItem) {
			html += '		<li>';
			html += '			<a href="javascript:void(0);" data-type="menu" data-id="' + menuItem.id + '" data-version="' + menuItem.version + '" data-mapping="' + menuItem.mapping + '" data-api-doc-id="' + menuItem.apiDocId + '" title="' + menuItem.desc + '">' + menuItem.desc + '</a>';
			html += '		</li>';
		});
		html += '	</ul>';
		html += '</li>';
	});
	$('#side-menu-content').html(html);

	initTabMenusEvents();

	history.replaceState(null, '接口文档', location.pathname + '?name=' + getParameter('name') + '&tab=' + menu.desc);
}

function initTabMenusEvents() {
	$('#side-menu-content').metisMenu();
	$('#side-menu-content li a[data-api-doc-id]').click(function() {
		initTabMenuContent($(this).text(), $(this).attr('data-api-doc-id'));
	});
    $('a[data-id]').dblclick(function () {
        initEditElement($(this));
    });
	$('#search-menu-btn').click(searchMenu);
	$('#search-menu-input').change(searchMenu);
}

function initEditElement(me) {
    if (me.children("input").length > 0) {
        return false;
    }
    var desc = me.text();
    var edit = document.createElement('input');
    edit.type = 'text';
    edit.value = desc;
    edit.style = "width: 100%;";
    edit.onblur = function() {
        updateDesc(me, this.value);
    };
    me.text('');
    me.append(edit);
    edit.setSelectionRange(0, desc.length);
    edit.focus();
}

function updateDesc(ele, desc) {
    ele.text(desc);

    var id = ele.attr('data-id');
    var type = ele.attr('data-type');
    var version = ele.attr('data-version');
    $.ajax({
        type: 'PUT',
        url: '/api-doc/' + type + '/' + id,
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'desc': desc,
            'version': version
        }),
        success: function (data) {
            if (data.success) {
                ele.attr('data-version', parseInt(version) + 1);
            } else {
                alert(data.message);
            }
        },
        error: function (xhr, errorType, error) {
            alert(error);
        }
    });
}

function searchMenu() {
	$('.sidebar .nav li').each(function() {
			$(this).removeClass('hidden');
		});
		var search = $('#search-menu-input').val();
		if(!search) {
			return;
		}

		$('.sidebar .nav-second-level li').each(function() {
			var link = $(this).find('a');
			if(link.attr('data-mapping').indexOf(search) < 0
				&& link.attr('title').indexOf(search) < 0) {
				$(this).addClass('hidden');

				var flag = true;
				$(this).parent().find('li').each(function() {
					if(!$(this).hasClass('hidden')) {
						flag = false;
					}
				});
				if(flag) {
					$(this).parent().parent().addClass('hidden');
				}
			}
		});
}

function initTabMenuContent(title, apiDocId) {
    $.ajax({
        type: 'GET',
        url: '/api-doc/' + apiDocId,
        success: function (data) {
            if (data.success) {
                $('#page-wrapper .page-header').text(title);
                $('#request-url').text(data.data.url);
                $('#request-action').text(data.data.action);
                $('#request-params').html(initTabMenuContentField(data.data.request, ""));
                $('#response-params').html(initTabMenuContentField(data.data.response, ""));
                $('#page-wrapper').removeClass('hidden');

                $('td[data-id]').dblclick(function () {
                    initEditElement($(this));
                });
            } else {
                alert(data.message);
            }
        },
        error: function (xhr, errorType, error) {
            alert(error);
        }
    });
}

function initTabMenuContentField(data, fillStr) {
	var html = '';
	$.each(data, function(i, item) {
		html += '<tr>';
		html += '    <td>' + fillStr + item.name + '</td>';
		html += '    <td>' + item.type + '</td>';
		html += '    <td>' + item.required + '</td>';
		html += '    <td data-type="field" data-id="' + item.id + '" data-version="' + item.version + '">' + item.desc + '</td>';
		html += '</tr>';

		if(item.type === 'object' || item.type === 'list') {
			html += initTabMenuContentField(item.childs, fillStr + '&emsp;');
		}
	});

	return html;
}