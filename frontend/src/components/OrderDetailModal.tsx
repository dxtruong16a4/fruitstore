import React from 'react';
import { X, Package, MapPin, User, Mail, Phone, Calendar, CreditCard } from 'lucide-react';
import { type OrderDetailResponse } from '../api/orderApi';

interface OrderDetailModalProps {
  order: OrderDetailResponse | null;
  isOpen: boolean;
  onClose: () => void;
  onCancelOrder?: (orderId: number) => void;
}

const OrderDetailModal: React.FC<OrderDetailModalProps> = ({
  order,
  isOpen,
  onClose,
  onCancelOrder
}) => {
  if (!isOpen || !order) return null;

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(price);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('vi-VN', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'DELIVERED': return 'bg-green-100 text-green-800';
      case 'CONFIRMED': return 'bg-blue-100 text-blue-800';
      case 'SHIPPED': return 'bg-purple-100 text-purple-800';
      case 'PENDING': return 'bg-yellow-100 text-yellow-800';
      case 'CANCELLED': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const canCancel = order.status === 'PENDING' || order.status === 'CONFIRMED';

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg max-w-4xl w-full max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex justify-between items-center p-6 border-b border-gray-200">
          <div>
            <h2 className="text-2xl font-bold text-gray-900">Chi tiết đơn hàng</h2>
            <p className="text-gray-600">#{order.orderNumber}</p>
          </div>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <X className="w-6 h-6" />
          </button>
        </div>

        {/* Content */}
        <div className="p-6 space-y-6">
          {/* Order Status */}
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-3">
              <Package className="w-5 h-5 text-gray-500" />
              <span className="text-lg font-semibold">Trạng thái đơn hàng</span>
            </div>
            <span className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(order.status)}`}>
              {order.status}
            </span>
          </div>

          {/* Order Info Grid */}
          <div className="grid md:grid-cols-2 gap-6">
            {/* Customer Info */}
            <div className="space-y-4">
              <h3 className="text-lg font-semibold text-gray-900 flex items-center">
                <User className="w-5 h-5 mr-2 text-gray-500" />
                Thông tin khách hàng
              </h3>
              <div className="space-y-2 text-sm">
                <div className="flex items-center">
                  <User className="w-4 h-4 mr-2 text-gray-400" />
                  <span className="font-medium">Tên:</span>
                  <span className="ml-2">{order.customerName}</span>
                </div>
                <div className="flex items-center">
                  <Mail className="w-4 h-4 mr-2 text-gray-400" />
                  <span className="font-medium">Email:</span>
                  <span className="ml-2">{order.customerEmail}</span>
                </div>
                {order.phoneNumber && (
                  <div className="flex items-center">
                    <Phone className="w-4 h-4 mr-2 text-gray-400" />
                    <span className="font-medium">SĐT:</span>
                    <span className="ml-2">{order.phoneNumber}</span>
                  </div>
                )}
                <div className="flex items-center">
                  <Calendar className="w-4 h-4 mr-2 text-gray-400" />
                  <span className="font-medium">Ngày đặt:</span>
                  <span className="ml-2">{formatDate(order.createdAt)}</span>
                </div>
              </div>
            </div>

            {/* Shipping Info */}
            <div className="space-y-4">
              <h3 className="text-lg font-semibold text-gray-900 flex items-center">
                <MapPin className="w-5 h-5 mr-2 text-gray-500" />
                Thông tin giao hàng
              </h3>
              <div className="text-sm">
                <div className="flex items-start">
                  <MapPin className="w-4 h-4 mr-2 text-gray-400 mt-0.5" />
                  <div>
                    <span className="font-medium">Địa chỉ:</span>
                    <p className="mt-1 text-gray-600">{order.shippingAddress}</p>
                  </div>
                </div>
                {order.notes && (
                  <div className="mt-3">
                    <span className="font-medium">Ghi chú:</span>
                    <p className="mt-1 text-gray-600">{order.notes}</p>
                  </div>
                )}
              </div>
            </div>
          </div>

          {/* Order Items */}
          <div className="space-y-4">
            <h3 className="text-lg font-semibold text-gray-900 flex items-center">
              <Package className="w-5 h-5 mr-2 text-gray-500" />
              Sản phẩm đã đặt ({order.orderItems.length} sản phẩm)
            </h3>
            <div className="border border-gray-200 rounded-lg overflow-hidden">
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Sản phẩm
                      </th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Giá
                      </th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Số lượng
                      </th>
                      <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Thành tiền
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {order.orderItems.map((item) => (
                      <tr key={item.orderItemId}>
                        <td className="px-4 py-4 whitespace-nowrap">
                          <div className="text-sm font-medium text-gray-900">
                            {item.product.name}
                          </div>
                        </td>
                        <td className="px-4 py-4 whitespace-nowrap text-sm text-gray-900">
                          {formatPrice(item.unitPrice)}
                        </td>
                        <td className="px-4 py-4 whitespace-nowrap text-sm text-gray-900">
                          {item.quantity}
                        </td>
                        <td className="px-4 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          {formatPrice(item.subtotal)}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          {/* Order Summary */}
          <div className="bg-gray-50 rounded-lg p-4">
            <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center">
              <CreditCard className="w-5 h-5 mr-2 text-gray-500" />
              Tổng kết đơn hàng
            </h3>
            <div className="space-y-2 text-sm">
              <div className="flex justify-between">
                <span>Số lượng sản phẩm:</span>
                <span>{order.totalItems} sản phẩm</span>
              </div>
              <div className="flex justify-between">
                <span>Loại sản phẩm:</span>
                <span>{order.orderItems.length} loại</span>
              </div>
              <div className="border-t border-gray-300 pt-2 flex justify-between text-lg font-semibold">
                <span>Tổng cộng:</span>
                <span className="text-green-600">{formatPrice(order.totalAmount)}</span>
              </div>
            </div>
          </div>

          {/* Action Buttons */}
          <div className="flex justify-end space-x-3 pt-4 border-t border-gray-200">
            <button
              onClick={onClose}
              className="px-4 py-2 text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-lg transition-colors"
            >
              Đóng
            </button>
            {canCancel && onCancelOrder && (
              <button
                onClick={() => onCancelOrder(order.orderId)}
                className="px-4 py-2 text-white bg-red-600 hover:bg-red-700 rounded-lg transition-colors"
              >
                Hủy đơn hàng
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderDetailModal;
