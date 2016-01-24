class BankAccount < ApplicationRecord
  validates_presence_of :name
  validates_presence_of :currency_code
end
