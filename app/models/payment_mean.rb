class PaymentMean < ApplicationRecord
  has_many :transactions

  validates_presence_of :name, :currency_code
end
