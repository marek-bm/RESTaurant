$(document).ready(function () {

    changeStatus();
    loadAllNotCompletedOrders();
    autocompleteProduct();
    loadReadyOrders();

    function loadAllNotCompletedOrders() {
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/order/',
            async: true,
            success: function (data) {
                //sort data
                console.log('success', data);
                var ordersFetched = data["_embedded"]["orderList"];
                var ordersOpen = [];
                var ordersInProgress = [];
                for (var i in ordersFetched) {
                    var order = ordersFetched[i];
                    order["status"] == "OPEN" ? ordersOpen.push(order) : ordersInProgress.push(order);
                    console.log('i', ordersFetched[i]);
                }
                appedToHtml(ordersOpen, '#openOrders');
                appedToHtml(ordersInProgress, '#in-progress');
            }
        });
    }

    function loadReadyOrders() {
        $.ajax({
            type : 'GET',
            url  : 'http://localhost:8080/order/ready',
            async: true,
            success: function (data) {
                console.log('ready order', data);
                var ordersReady = data["_embedded"]["orderList"];
                console.log('orders ready', ordersReady);
                appedToHtml(ordersReady,'#ready-to-go');
            }
        });
    }

    function appedToHtml(data, sectionId) {
        // console.log("data in appentToHtml", data);
        for (var i in data) {
            var status = data[i]["status"];
            var products = data[i]["products"];
            var links = data[i]["_links"];
            var totalSum = data[i]["totalSum"];
            var inProgressUrl = links["in-progress"]['href'];
            var readyUrl = links["ready"]['href'];
            var deliveredUrl = links["delivered"]['href'];
            console.log('order', products);
            console.log('delivered url', deliveredUrl);

            if (status == 'OPEN') {
                var header = status + ' id: ' + data[i].id + ' Total: ' + totalSum;
                var html = '<div class="card">'
                html += '<div class="card-header bg-danger text-white"> ' + header + '</div>';
                html += '<div class="card-body bg-light">';
                for (var i in products) {
                    html += '<ul> ' + products[i].product.name + " x" + products[i].quantity + ' </ul>'
                }
                html += '</div>'; // closed body
                html += '<div class="card-footer bg-danger text-white">';
                html += '<button id="progressing" formaction=' + inProgressUrl + '>Start processing</button> ';
                html += '</div>'; //closed footer
                html += '</div>'; //closed panel
                $(sectionId).append(html);

            }

            if (status == 'IN_PROGRESS') {
                var header = status + ' id: ' + data[i].id + ' Total: ' + totalSum;
                var html = '<div class="card">'
                html += '<div class="card-header bg-warning text-white"> ' + header + '</div>';
                html += '<div class="card-body bg-light">';
                for (var i in products) {
                    html += '<ul> ' + products[i].product.name + " x" + products[i].quantity + ' </ul>'
                }
                html += '</div>'; // closed body
                html += '<div class="card-footer bg-warning text-white">';
                html += '<button id="complete" formaction=' + readyUrl + '>Completed</button>';
                html += '</div>'; //closed footer
                html += '</div>'; //closed panel
                $(sectionId).append(html);
            }

            if (status == 'READY'){
                var header = status + ' id: ' + data[i].id + ' Total: ' + totalSum;
                var html = '<div class="card panel-ready">';
                html += '<div class="card-header bg-success text-white"> ' + header + '</div>';
                html += '<div class="card-body bg-light">';
                for (var i in products) {
                    html += '<ul> ' + products[i].product.name + " x" + products[i].quantity + ' </ul>'
                }
                html += '</div>'; // closed body
                html += '<div class="card-footer bg-success">';
                html += '<button id="delivered" formaction=' + deliveredUrl + '>Pick-up</button>';
                html += '</div>'; //closed footer
                html += '</div>'; //closed panel
                $(sectionId).append(html);
            }
        }
    }

    $('#ready-to-go').click(function (event) {
        var node = event.target.nodeName;
        var id = event.target.id;
        var cl = event.target.className;
        var parentNode = event.target.parentNode;
        var parentElement = event.target.parentElement;

        console.log('ready to go node',node);
        console.log('ready to id',id);
        console.log('ready to class',cl);
        console.log('parent node', parentNode);
        console.log('parent elemtn', parentElement);
        if (node=='BUTTON'){
            $('#'+id).closest('.card').remove();
        }
    });

    function changeStatus() {
        $('#openOrders,#in-progress,#ready-to-go').click(function (event) {
            var element = $(event.target.nodeName);
            var tag = event.target.tagName;
            var id = event.target.id;
            console.log('tag', tag);
            console.log('id', id);
            if (element.is('BUTTON')) {
                var link = event.target.getAttribute("formaction");
                // console.log('PUT link', link);
                $.ajax({
                    type: 'PUT',
                    url: link,
                    async: true,
                    data: "",
                    contentType: "application/json; charset=utf-8"
                });
                window.location.reload();
            }
        });
    }

    function autocompleteProduct() {
        $('tbody').on('click', function (event) {
            var attrId = event.target.id;
            var id = attrId.split('_')[1];

            $('#product-name_' + id).autocomplete({
                source: function (request, callBack) {
                    $.ajax({
                        type: 'GET',
                        url: 'http://localhost:8080/products/',
                        dataType: 'json',
                        success: function (data) {
                            var products = [{
                                label: 'No matchin values for ' + request.term,
                                value: ''
                            }];
                            console.log('products - before', data);

                            var fetched = data["_embedded"]["productList"];
                            if (fetched.length) {
                                products = $.map(fetched, function (object) {
                                    return {
                                        label: object.name,
                                        value: object.name,
                                        properties: object
                                    }
                                });
                            }
                            console.log('products -formatted', products);
                            callBack(products);
                        }
                    });
                },
                minLength: 2,
                select: function (event, selectetData) {
                    console.log('selected data ', selectetData);

                    if (selectetData && selectetData.item && selectetData.item.properties) {
                        //product-name
                        var data = selectetData.item.properties;
                        var gross = data.netPrice * (1 + data.tax);
                        var qty = 1;
                        $('#quantity_' + id).val(qty);
                        $('#product-name_' + id).val(data.name);
                        $('#net-price_' + id).val(data.netPrice);
                        $('#tax_' + id).val(data.tax);
                        $('#gross-price_' + id).val(gross);
                        $('#sub-total_' + id).val(qty * gross);
                        $('#product-id_' + id).val(data.productId);
                        console.log('prod id', data.productId);
                        calculateTotal();
                    }
                }
            });
        });
    }

    //price change
    $('table').on('change keyup blur', ".form-control", function () {
        var id_attr = $(this).attr('id');
        console.log('id_attr', id_attr);
        var id = id_attr.split('_')[1];
        var quantity = parseFloat($('#quantity_' + id).val());
        var price = parseFloat($('#gross-price_' + id).val());
        $('#sub-total_' + id).val((quantity * price).toFixed(2));
        calculateTotal();
    });


    //add row to the table
    var i = $('#autocomplete_table tr').length;
    $('#addNew').on('click', function () {
        var html = '<tr id="row_' + i + '">';
        // html += '<td id="delete_"+ i + scope="row" class="delete_row"><img src="./minus.svg" alt="---"></td>';
        html += '<th id="delete_' + i + '" scope="row" class="delete_row"><button id="del-btn_' + i + '" class="btn btn-danger" > - </button></th>';
        html += '<td><input type="text" class="form-control product-name" name="product-name" id="product-name_' + i + '" placeholder="Product Name"></td>';
        html += '<td><input type="number" class="form-control net-price" name="net-price" id="net-price_' + i + '" placeholder="Net price"></td>';
        html += '<td><input type="number" class="form-control tax" name="tax" id="tax_' + i + '" placeholder="Tax"></td>';
        html += '<td><input type="number" class="form-control gross-price" name="gross-price" id="gross-price_' + i + '" placeholder="Gross price"></td>';
        html += '<td><input type="number" class="form-control quantity" name="quantity" id="quantity_' + i + '" min="1" placeholder="Quantity" ></td> ';
        html += '<td><input type="number" class="form-control sub-total" name="sub-total" id="sub-total_' + i + '" placeholder="Total"></td>';
        html += '<td hidden> <input type="text" id="product-id_' + i + '" class="product-id"></td>';
        html += '</tr>';
        $('#autocomplete_table').append(html);
        i++;
    });

    //remove table row
    $('table').click(function (event) {
        var node = event.target.nodeName;
        if (node == 'BUTTON') {
            var id = event.target.id;
            $('#' + id).parents('tr').remove();
            calculateTotal();
        }
    });

    function calculateTotal() {
        var total = 0;
        $('.sub-total').each(function () {
            total += parseFloat($(this).val());
        });
        $('#totalSum').val(total);
    }

    //make receipt
    $('#makeOrder').on('click', function () {
        var table = $('#autocomplete_table');
        var rows = table.find('tbody tr');
        var order = {
            products: [],
            totalSum: $('#totalSum').val()
        };

        rows.each(function () {
            var product = {
                productId: $(this).find('.product-id').val(),
                name: $(this).find('.product-name').val(),
                netPrice: $(this).find('.net-price').val(),
                tax: $(this).find('.tax').val(),
                grossPrice: $(this).find('.gross-price').val()
            };

            var quantity = $(this).find('.quantity').val();
            var subTotal = $(this).find('.sub-total').val();
            var orderItem = {};
            orderItem.product = product;
            orderItem.quantity = quantity;
            orderItem.subTotal = subTotal;
            order.products.push(orderItem);
        });
        console.log('order', order);
        console.log('order stringify', JSON.stringify(order));

        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/order/',
            async: true,
            data: JSON.stringify(order),
            // data: order,
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        });
        window.location.reload();
    });
});
