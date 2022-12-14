// JavaScript Document

var b = 0;
var c = 0;
if ($(".confirm-pay").size() > 0) {
	b = $(".confirm-pay").offset().top;
	c = $(".confirm-pay").outerHeight();
}

$(function() {

	// 移除收货地址提示背景类
	$("body").on('click', '#addressinfo', function() {
		$(this).removeClass('bgcolor');
	})

	// 新增收货地址弹框
	$("body").on('click', '.addr-add', function() {
		$('.addr-box').show();
		$('.bg').show();
		showAddressHtml();
	})

	$("body").on('click', '.addr-box-oprate', function() {
		$('.addr-box').hide();
		$('.bg').hide();
	})

	// 编辑收货址
	$("body").on('click', '.address-edit', function() {
		$('.addr-box').show();
		$('.bg').show();
		var address_id = $(this).data('address-id');
		var active_address_id = $(".address-list").find(".address-box").filter(".active").data("address-id");

		$.get('/user/address/edit.html', {
			address_id: address_id
		}, function(result) {
			if (result.code == 0) {
				$('#edit-address-div').html(result.data);
				$("#btn_validate").click(function() {
					reloadUserAddress();
				});
			}
		}, "json");

		return false;
	})

	// 删除收货地址
	$("body").on('click', '.address-delete', function() {
		var address_id = $(this).data('address-id');
		var active_address_id = $(".address-list").find(".address-box").filter(".active").data("address-id");

		var box = $(this).parents(".address-box");
		$.confirm("您确定要删除此收货地址吗？", function() {
			$.get('/user/address/del.html', {
				address_id: address_id
			}, function(result) {
				if (result.code == 0) {
					if (active_address_id == address_id) {
						changeAddress(0);
					} else {
						reloadUserAddress();
					}

				}
				$.msg(result.message);
			}, "json");
		});
		return false;
	})

	// 设置收货地址为默认
	$("body").on('click', '.address-default', function() {
		var address_id = $(this).data('address-id');
		$.get('/user/address/set-default', {
			address_id: address_id
		}, function(result) {
			if (result.code == 0) {
				reloadUserAddress();
			}
			$.msg(result.message);
		}, "json");

		return false;
	})

	// 选择收货地址
	$("body").on('click', '.address-box', function() {
		var address_id = $(this).data('address-id');
		$(this).siblings().removeClass("active");
		$(this).addClass("active");
		//changeAddress(address_id);
	})

	// 更多收货地址展开收缩效果
	$("body").on('click', '.addr-more', function() {
		$(this).find('i').toggleClass('active');
		$(this).find('span').html($(this).find('i').hasClass('active') ? "收起收货地址" : "展开收货地址");
		$('.address-more').toggle();
	});

	// 设置最佳送货时间
	$("#set_best_time").click(function() {
		return false;
	});

	// 设置最佳送货时间
	$("body").on('click', '.best_time', function() {
		if ($(this).is(":checked")) {
			var send_time_id = $(this).val();
			var send_time = '';

			$(".box").removeClass('active').removeClass('active2');
			// 指定送货时间，去掉后面的日期
			$(".best-time-desc").html('');
			// 指定送货时间，去掉已经选的日期
			$(".set_time").removeClass('current');
			$(this).parent().parent().addClass('active');
			//setBestTime(send_time_id, send_time);
		}
	})

	// 设置最佳送货时间范围
	$("body").on('click', '.set_time', function() {
		$(".set_time").removeClass('current');
		$(this).addClass('current');

		$(".box").removeClass('active');
		$("#set_best_time").prop('checked', true);
		$("#set_best_time").parent().parent().addClass('active');

		var send_time_id = $("#set_best_time").val();
		var send_time = $(this).attr('data');

		setBestTime(send_time_id, send_time);
	})

	// 设置店铺配送方式
	$("body").on('change', '.shipping-select', function() {

		var shipping_list = [{
			shop_id: $(this).data("shop-id"),
			shipping_id: $(this).val(),
		}];

		changePayment({
			shipping_list: shipping_list
		});

	})

	// 设置店铺使用红包
	$("body").on('change', '.shop-bonus', function() {
		var bonus_list = [{
			shop_id: $(this).data('shop-id'),
			bonus_id: $(this).val()
		}];

		changePayment({
			bonus_list: bonus_list
		});
	});

	// 设置系统使用红包
	$("body").on('click', '.system-bonus', function() {

		$(this).parent("li").siblings().removeClass("current");
		$(this).parent("li").toggleClass("current");

		var user_bonus_id = 0;

		if ($(this).parent("li").hasClass("current")) {
			user_bonus_id = $(this).data("user-bonus-id");
		}

		var bonus_list = [{
			shop_id: 0,
			bonus_id: user_bonus_id
		}];

		changePayment({
			bonus_list: bonus_list
		});
	})

	// 修改发票内容事件
	$("body").on('click', '.inv-info .modify', function() {
		$('.invoice-box').show();
		$('.bg').show();
	})

	// 发票选弹框关闭事件
	$("body").on('click', '.invoice-box-oprate', function() {
		$('.invoice-box').hide();
		$('.bg').hide();
	})

	// 发票标题点击切换选中状态
	$("body").on('click', '.invoice-title', function() {
		$(this).addClass('invoice-item-selected').siblings().removeClass('invoice-item-selected');
		if ($('#add-invoice').hasClass('invoice-item-selected')) {
			$('#save-invoice').removeClass('hide').find('input').removeAttr('readonly').val('').focus();
		} else {
			$('#save-invoice').addClass('hide');
			$('#add-invoice').val('');
		}
		// 选中单选按钮
		$('.invoice-title').find(":radio").prop("checked", false);
		$(this).find(":radio").prop("checked", true);
	})

	// 普通发票内容切换
	$("body").on('click', '.invoice-type', function() {
		$(this).addClass('invoice-item-selected').siblings().removeClass('invoice-item-selected');
		// 选中单选按钮
		$(this).find(":radio").prop("checked", true);
	})

	// 保存发票设置
	$("body").on('click', '#inv_submit', function() {
		changeInvoice();
	})

	// 取消发票
	$("body").on('click', '#inv_cancle', function() {
		$('.invoice-box').hide();
		$('.bg').hide();
	})

	// 选择发票类型
	$("body").on('click', '.tab-nav-item', function() {
		if ($(this).hasClass('disabled') == false) {
			$(this).addClass('tab-item-selected').siblings().removeClass('tab-item-selected');
			var invoice_type = $(this).data('invoice-type');
			$('.form-horizontal').hide();
			$("#invoice_type_" + invoice_type).show();
		}
	})

	// 支付方式积分选中事件
	$("body").on('change', '#integral_enable', function() {
		changePayment();
	});

	// 改变积分值事件
	$("body").on('blur', '#integral', function() {
		changePayment();
	});

	// 支付方式余额选中事件
	$("body").on('change', '#balance_enable', function() {
		if ($(this).is(":checked")) {
			$(".SZY-BALANCE-PASSWORD").show();
		} else {
			$(".SZY-BALANCE-PASSWORD").hide();
		}
		changePayment();
	});

	// 支付方式余额选中事件
	$("body").on('blur', '#balance_password', function() {
		if ($(this).val() == '') {
			$(this).addClass("error");
		} else {
			$(this).removeClass("error");
		}
	});

	// 改变余额值事件
	$("body").on('blur', '#balance', function() {
		changePayment();
	})

	// 结算页面提交

	// 设置支付方式
	$("body").on('click', '.pay_code', function() {
		changePayment();
	})

	// 移除支付方式提示背景类
	$("body").on('click', '#pay_bank', function() {
		$(this).removeClass('bgcolor');
	})

	// 付款信息弹框关闭事件
	$("body").on('click', '.payment-box-oprate', function() {
		$('.payment-box').hide();
		$('.bg').hide();
	})

	// showAddress();

	if ($(".confirm-pay").size() > 0) {
		// 结算页面提交按钮滚动悬浮效果
		b = $(".confirm-pay").offset().top;
		c = $(".confirm-pay").outerHeight();
		$(window).scroll(function(event) {
			resetSubmitPosition(b, c);
		})
	}

	// 平台红包使用取消勾选
	// $('.platform-item').hover(function(){
	// if($(this).parent("li").hasClass("platform-current")){
	// $(this).find(".item-cancel").toggleClass("hide");
	// }else{
	// $(".item-cancel").addClass("hide");
	// }
	// })
	// 平台红包—限品类
	$(".range-use").hover(function() {
		$(this).find(".platform-type-tips").toggleClass("hide");
	});
	// 平台红包点击收缩效果
	$('.platform-box .title').on('click', function() {
		$(this).find('.arrow').toggleClass('active').parent('.title').next('.platform-list').slideToggle("normal", function() {
			resetSubmitPosition();
		});
		resetSubmitPosition();
	});

});

function resetSubmitPosition(bb, cc) {
	if ($(".confirm-pay").size() > 0) {
		if (bb == undefined || cc == undefined) {
			b = $(".confirm-pay").offset().top;
			c = $(".confirm-pay").outerHeight();
		} else {
			b = bb;
			c = cc;
		}
		var d = $(window).height();
		var e = $(window).scrollTop();
		var f = $('.site-footer').height();
		b - d - e + c - 10 > 0 ? ($(".confirm-pay").addClass("bottom")) : ($(".confirm-pay").removeClass("bottom"));
	}
}

/**
 * 重载用户地址
 */
function reloadUserAddress() {
	$.get('/checkout/user-address', {}, function(result) {
		if (result.code == 0) {
			$("#user_address_list").replaceWith($.parseHTML(result.data));
		}
	}, "json");
}

// 设置最佳送货时间
function setBestTime(send_time_id, send_time) {
	$.post('/checkout/change-best-time', {
		send_time_id: send_time_id,
		send_time: send_time
	}, function(result) {
		if (result.code == 0) {
			if (send_time.length > 0) {
				$('.seltimebox').parent().find("font").html(send_time);
				$('.seltimebox').parent().removeClass('active').addClass('active2');
			}
		}
	}, "json");
}

// 调用发货地址添加地址显示层
function showAddressHtml() {
	$.ajax({
		type: 'GET',
		url: '/user/address/add',
		dataType: 'json',
		success: function(result) {
			if (result.code == 0) {
				$('#edit-address-div').html(result.data);
			}
		}
	});
}

// 取消添加地址
function cancel() {
	$('.addr-box').hide();
	$('.bg').hide();
}

// 设置本次订单的收货地址
function changeAddress(address_id) {
	$.post('/checkout/change-address', {
		address_id: address_id
	}, function(result) {
		if (result.code == 0) {
			$.go(window.location.href);
		} else {
			$.msg(result.message, {
				time: 5000
			})
		}
	}, 'json');
}

// 设置店铺配送方式
function changeShipping(shipping_id, shop_id) {
	$.post('/checkout/change-shipping', {
		shop_id: shop_id,
		ship_id: shipping_id
	}, function(result) {
		if (result.code == 0) {
			// 渲染支付信息
			renderPayment(result.user_info, result.order, result.shop_orders, false, false);
		}
	}, "json");
}

// 设置店铺红包活动
function set_shop_bonus(bonus_id, shop_id) {
	$.ajax({
		type: 'GET',
		url: '/checkout/default/set-shop-bonus',
		data: {
			shop_id: shop_id,
			bonus_id: bonus_id
		},
		dataType: 'json',
		success: function(result) {
			if (result.code == 0) {
				$("#shop_count_" + shop_id).html(result.data.shop_count);
				$("#order_amount").html(result.data.count)
				$("#pay_balance").html(result.data.pay_balance)
				$("#pay_paypoint").html(result.data.pay_point)
				$("#pay_count").html(result.data.pay_count)
				$("#checkout_amount").html(result.data.checkout_amount)
			}
		}
	});
}

// 变更设置支付信息
function changePayment(data) {

	var integral_enable = $("#integral_enable").is(":checked");
	var balance_enable = $("#balance_enable").is(":checked");
	var balance = $("#balance").val();
	var integral = $("#integral").val();
	var pay_code = $(".pay_code:checked").val();

	if (!data) {
		data = {}
	}

	// $.post("/checkout/change-payment", {
	// 	integral: integral,
	// 	integral_enable: integral_enable ? 1 : 0,
	// 	balance: balance,
	// 	balance_enable: balance_enable ? 1 : 0,
	// 	pay_code: pay_code,
	// 	shipping_list: data.shipping_list,
	// 	bonus_list: data.bonus_list
	// }, function(result) {
	// 	var user_info = result.user_info;
	// 	var order = result.order;
	// 	var shop_orders = result.shop_orders;
	// 	if (result.code == 0) {
	//
	// 		// 渲染支付信息
	// 		renderPayment(user_info, order, shop_orders, balance_enable, integral_enable);
	//
	// 		if (result.message != null && $.trim(result.message).length > 0) {
	// 			$.msg(result.message);
	// 		}
	// 	} else {
	// 		$.msg(result.message);
	// 	}
	// }, "json");

	if (balance_enable) {
		$(".SZY-BALANCE-INFO").css("display", "inline-block");
	} else {
		$(".SZY-BALANCE-INFO").css("display", "none");
	}
}

function renderPayment(user_info, order, shop_orders, balance_enable, integral_enable) {
	$("#balance").val(order.balance);

	$(".SZY-ORDER-BALANCE").not(":input").html(order.balance_format);

	$(".SZY-USER-BALANCE").html(user_info.balance_format);
	// 剩余应付金额
	$(".SZY-ORDER-MONEY-PAY").html(order.money_pay_format);
	// 订单总金额
	$(".SZY-ORDER-AMOUNT").html(order.order_amount_format);
	// 红包总金额
	$(".SZY-ORDER-BONUS-AMOUNT").html(order.total_bonus_amount_format);
	// 货到付款加价
	if (order.is_cod == 1 && parseInt(order.cash_more) > 0) {
		$(".SZY-ORDER-CASH-MORE-AMOUNT").show();
	} else {
		$(".SZY-ORDER-CASH-MORE-AMOUNT").hide();
	}

	if (shop_orders && $.isArray(shop_orders)) {
		for (var i = 0; i < shop_orders.length; i++) {
			// 订单总金额
			$(".SZY-SHOP-ORDER-AMOUNT-" + shop_orders[i].shop_id).html(shop_orders[i].order_amount_format);
			// 红包金额
			$(".SZY-SHOP-BONUS-AMOUNT-" + shop_orders[i].shop_id).html("- " + shop_orders[i].shop_bonus_amount_format);
		}
	}

	if (order) {
		$(".SZY-BONUS-NAME").html(order.bonus_name);
		$(".SZY-BONUS-AMOUNT").html("- " + order.bonus_amount_format);
		if (order.bonus_id > 0) {
			$(".SZY-BONUS-AMOUNT-CONTAINER").show();
		} else {
			$(".SZY-BONUS-AMOUNT-CONTAINER").hide();
		}
	}

	if (order.money_pay > 0) {
		$("#balance_money_pay").show();
		$("#paylist").show();
	} else {
		$("#balance_money_pay").hide();
		$("#paylist").hide();
		$("#pay_bank").removeClass('bgcolor');
	}

	if (balance_enable) {
		$(".SZY-BALANCE-INFO").css("display", "inline-block");
	} else {
		$(".SZY-BALANCE-INFO").css("display", "none");
	}

	// 计算提交按钮的位置
	resetSubmitPosition();
}

// 发票调用
function show_invoice() {
	$.ajax({
		type: 'GET',
		url: '/checkout/default/show-invoice',
		data: {},
		dataType: 'json',
		success: function(result) {
			if (result.code == 0) {
				$(".invoice-coupon").html(result.data);
			}
		}
	});
}

// 发票内容获取
function changeInvoice() {

	var invoice = {};
	var inv_type = $('.tab-item-selected').data('invoice-type');

	if (inv_type == 1) {
		// 普通发票
		invoice = $("#invoice_type_1").serializeJson();
		if (invoice.inv_title != "个人") {
			if ($.trim(invoice.inv_company) == "") {
				$.msg("单位名称不能为空");
				$("#invoice_type_1").find("#inv_company").focus();
				return;
			} else {
				invoice.inv_title = $.trim(invoice.inv_company);
			}
		}
	} else if (inv_type == 2) {
		var validator = $("#invoice_type_2").validate();
		if (!validator.form()) {
			return;
		}
		invoice = $("#invoice_type_2").serializeJson();
	}

	$.post('/checkout/change-invoice', invoice, function(result) {
		if (result.code == 0) {
			if (result.data == null || result.data == '') {
				result.data = [];
			}
			var html = "<span>" + result.data.join("</span><span>") + "</span>"
			$('.inv-info').find("span").remove();
			$('.inv-info').prepend(html);

			// 成功后关闭
			$('.invoice-box').hide();
			$('.bg').hide();
		} else {
			$.msg(result.message, {
				time: 5000
			});
		}
	}, "json");

	return invoice;

}