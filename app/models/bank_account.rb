class BankAccount < ApplicationRecord
  has_many :transactions

  validates_presence_of :name
  validates_presence_of :currency_code
end
